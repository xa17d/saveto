package at.xa1.saveto.common.android

import android.content.Intent
import at.xa1.saveto.common.navigation.HostHolder
import kotlin.coroutines.suspendCoroutine

typealias ResultTransform<R> = (resultCode: Int, data: Intent?) -> R

class IntentManager(
    private val hostHolder: HostHolder
) {
    suspend fun <R> startForResult(
        intentProvider: (host: HostHolder.Host) -> Intent,
        resultTransform: ResultTransform<R>
    ): R = suspendCoroutine { continuation ->
        hostHolder.runOrEnqueue {
            val intent = intentProvider(this)
            @Suppress("DEPRECATION")
            activity.startActivityForResult(intent, requestCode)
            map[requestCode] = { resultCode, data ->
                val result = runCatching { resultTransform(resultCode, data) }
                continuation.resumeWith(result)
            }

            requestCode++
            // TODO reset counter
        }
    }

    suspend fun <R> startForResult(
        intent: Intent,
        resultTransform: ResultTransform<R>
    ): R = startForResult(
        intentProvider = { intent },
        resultTransform = resultTransform
    )

    private val map = HashMap<Int, ResultTransform<Any?>>()

    private var requestCode = 100

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val action = map[requestCode] ?: return // TODO log
        action(resultCode, data)
    }
}
