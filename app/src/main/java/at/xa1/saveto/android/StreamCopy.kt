package at.xa1.saveto.android

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class StreamCopy(
    private val contentResolver: ContentResolver,
    private val bufferSize: Int = 200 * 1024 // 200KB
) {
    private val _progress = MutableStateFlow(Progress(isFinished = false, isFailed = false, progress = 0f))
    val progress: StateFlow<Progress>
        get() = _progress

    fun copy(sourceUri: Uri, destinationUri: Uri) {
        try {
            val source = contentResolver.openInputStream(sourceUri).use { source ->
                source ?: error("openInputStream(sourceUri) returns null")

                contentResolver.openOutputStream(destinationUri, "w").use { destination ->
                    destination ?: error("openOutputStream(destinationUri) returns null")
                    source.copyTo(destination, bufferSize = bufferSize)

                    source.close()
                    destination.close()
                }
            }
        } catch (e: IOException) {
            _progress.value = _progress.value.copy(isFailed = true) // TODO avoid race
        } finally {
            _progress.value = _progress.value.copy(isFinished = true)// TODO avoid race
        }
    }

    private fun copy(source: InputStream, destination: OutputStream): Long {
        var bytesCopied: Long = 0
        val buffer = ByteArray(bufferSize)
        do {
            val bytes = source.read(buffer)
            destination.write(buffer, 0, bytes)
            bytesCopied += bytes
        } while (bytes > 0)
        return bytesCopied
    }

    data class Progress(
        val isFinished: Boolean,
        val isFailed: Boolean,
        val progress: Float
    )
}
