package at.xa1.saveto.feature.save

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class StreamCopy(
    private val contentResolver: ContentResolverWrapper,
    private val bufferSize: Int = 200 * 1024 // 200KB
) {
    private val _progress = MutableStateFlow(initialProgress())
    val progress: StateFlow<Progress>
        get() = _progress

    private fun updateProgressError(type: StreamCopyErrorType, exception: Exception) {
        val oldValue = _progress.value

        // don't overwrite an already existing error.
        if (oldValue.error == null) {
            _progress.value = oldValue.copy(
                isFinished = true,
                error = StreamCopyError(type, exception)
            )
        }
    }

    fun copy(string: String, destinationUri: Uri) {
        val source = ByteArrayInputStream(string.toByteArray())
        copy(source, destinationUri)
    }

    fun copy(sourceUri: Uri, destinationUri: Uri) {
        val sourceStream = try {
            contentResolver.openInputStream(sourceUri)
        } catch (e: IOException) {
            updateProgressError(StreamCopyErrorType.OPEN_INPUT_STREAM_ERROR, e)
            return
        }

        copy(sourceStream, destinationUri)
    }

    private fun copy(sourceStream: InputStream, destinationUri: Uri) {
        val destinationStream = try {
            contentResolver.openOutputStream(destinationUri)
        } catch (e: IOException) {
            updateProgressError(StreamCopyErrorType.OPEN_OUTPUT_STREAM_ERROR, e)
            return
        }
        copy(sourceStream, destinationStream)
    }

    private fun copy(sourceStream: InputStream, destinationStream: OutputStream) {
        try {
            sourceStream.use { source ->
                try {
                    destinationStream.use { destination ->
                        try {
                            copyBytes(source = source, destination = destination)
                        } catch (e: IOException) {
                            updateProgressError(StreamCopyErrorType.COPY_ERROR, e)
                        }

                        source.close()
                        destination.close()
                    }
                } catch (e: IOException) {
                    updateProgressError(StreamCopyErrorType.OPEN_OUTPUT_STREAM_ERROR, e)
                }
            }
        } catch (e: IOException) {
            updateProgressError(StreamCopyErrorType.OPEN_INPUT_STREAM_ERROR, e)
        } finally {
            _progress.value = _progress.value.copy(isFinished = true) // TODO avoid race
        }
    }

    private fun copyBytes(source: InputStream, destination: OutputStream): Long {
        var progress = initialProgress()

        var bytesCopied: Long = 0
        val buffer = ByteArray(bufferSize)
        var bytes = source.read(buffer)
        while (bytes >= 0) {
            destination.write(buffer, 0, bytes)
            bytesCopied += bytes
            bytes = source.read(buffer)

            // TODO don't update after each increment, only max 10x per second.
            progress = progress.copy(bytesCopied = bytesCopied)
            _progress.value = progress
        }
        return bytesCopied
    }

    data class Progress(
        val isFinished: Boolean,
        val error: StreamCopyError?,
        val bytesCopied: Long
    )

    private fun initialProgress(): Progress =
        Progress(
            isFinished = false,
            error = null,
            bytesCopied = 0L
        ) // TODO add phases: e.g. start reading, closing,...
}

val StreamCopy.Progress.isFailed: Boolean
    get() = error != null

data class StreamCopyError(
    val type: StreamCopyErrorType,
    val exception: Exception
)

enum class StreamCopyErrorType {
    OPEN_INPUT_STREAM_ERROR,
    OPEN_OUTPUT_STREAM_ERROR,
    COPY_ERROR
}

interface ContentResolverWrapper { // TODO bettername
    fun openInputStream(sourceUri: Uri): InputStream
    fun openOutputStream(destinationUri: Uri): OutputStream
}

class AndroidContentResolver(
    private val contentResolver: ContentResolver
) : ContentResolverWrapper {

    override fun openInputStream(sourceUri: Uri): InputStream {
        return contentResolver.openInputStream(sourceUri)
            ?: throw IOException("Android Exception: openInputStream($sourceUri) returns null")
    }

    override fun openOutputStream(destinationUri: Uri): OutputStream {
        return contentResolver.openOutputStream(destinationUri, "w")
            ?: throw IOException("Android Exception: openOutputStream($destinationUri) returns null")
    }
}
