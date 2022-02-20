package at.xa1.saveto.feature.save

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Test

class StreamCopyTest {
    @Test
    fun `basic`() {
        val fakeContentResolver = FakeContentResolver()

        val streamCopy = StreamCopy(fakeContentResolver)
        streamCopy.copy(
            Uri.parse("test://source"),
            Uri.parse("test://destination"),
        )

        val progress = streamCopy.progress.value

        assertEquals(true, progress.isFinished)
        assertEquals(false, progress.isFailed)
        assertEquals(fakeContentResolver.inputData.size.toLong(), progress.bytesCopied)
    }
}
