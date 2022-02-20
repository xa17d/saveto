package at.xa1.saveto.feature.save

import java.io.IOException
import java.io.OutputStream

class FailingOutputStream : OutputStream() {
    override fun write(b: Int) {
        throw IOException("TEST write")
    }
}
