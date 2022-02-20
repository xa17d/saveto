package at.xa1.saveto.feature.save

import android.net.Uri
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

class FakeContentResolver : ContentResolverWrapper {

    var inputData: ByteArray = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
    var outputStream: OutputStream = ByteArrayOutputStream(0)

    private var inputConsumed = false
    private var outputConsumed = false

    override fun openInputStream(sourceUri: Uri): InputStream {
        if (inputConsumed) {
            error("openInputStream was called multiple times")
        }
        inputConsumed = true
        return ByteArrayInputStream(inputData)
    }

    override fun openOutputStream(destinationUri: Uri): OutputStream {
        if (outputConsumed) {
            error("openOutputStream was called multiple times")
        }
        outputConsumed = true
        return outputStream
    }
}
