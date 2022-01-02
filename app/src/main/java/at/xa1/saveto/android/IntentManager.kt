package at.xa1.saveto.android

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.coroutines.suspendCoroutine

typealias HostAction = IntentManager.Host.() -> Unit
typealias ResultTransform<R> = (resultCode: Int, data: Intent?) -> R

class IntentManager {

    suspend fun <R> startForResult(
        intent: Intent,
        resultTransform: ResultTransform<R>
    ): R = suspendCoroutine { continuation ->
        runOrEnqueue {
            context.startActivityForResult(intent, requestCode)
            map[requestCode] = { resultCode, data ->
                val result = runCatching { resultTransform(resultCode, data) }
                continuation.resumeWith(result)
            }

            requestCode++
            // TODO reset counter
        }
    }

    private val map = HashMap<Int, ResultTransform<Any?>>()

    private var requestCode = 100
    private var context: ComponentActivity? = null
    private val enqueuedActions = mutableListOf<HostAction>()
    fun runOrEnqueue(action: HostAction) {
        enqueuedActions.add(action)
        tryExecute()
    }

    private fun tryExecute() {
        val context = this.context
        if (context != null) {
            val host = Host(context)
            while (enqueuedActions.isNotEmpty()) {
                enqueuedActions.removeAt(0).invoke(host)
            }
        }
    }

    fun attach(context: ComponentActivity) {
        if (this.context != null) {
            error("already attached")
        }

        this.context = context

        context.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)

                detach(context)
            }
        })
    }

    private fun detach(context: Context) {
        if (context == this.context) {
            this.context = null
        } else {
            error("can only detach attached context")
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val action = map[requestCode] ?: return // TODO log
        action(resultCode, data)
    }

    class Host(val context: ComponentActivity)
}

