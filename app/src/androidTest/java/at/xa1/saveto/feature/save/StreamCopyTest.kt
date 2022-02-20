package at.xa1.saveto.feature.save

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Test

class StreamCopyTest {
    @Test
    fun successfulCopy() {
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

    @Test
    fun errorInputContentResolver() {
        val fakeContentResolver = FakeContentResolver()

        fakeContentResolver.failInOpenInputStream = true

        val streamCopy = StreamCopy(fakeContentResolver)
        streamCopy.copy(
            Uri.parse("test://source"),
            Uri.parse("test://destination"),
        )

        val progress = streamCopy.progress.value

        assertEquals(true, progress.isFinished)
        assertEquals(true, progress.isFailed)
        assertEquals(StreamCopyErrorType.OPEN_INPUT_STREAM_ERROR, progress.error!!.type)
    }

    @Test
    fun errorOutputContentResolver() {
        val fakeContentResolver = FakeContentResolver()

        fakeContentResolver.failInOpenOutputStream = true

        val streamCopy = StreamCopy(fakeContentResolver)
        streamCopy.copy(
            Uri.parse("test://source"),
            Uri.parse("test://destination"),
        )

        val progress = streamCopy.progress.value

        assertEquals(true, progress.isFinished)
        assertEquals(true, progress.isFailed)
        assertEquals(StreamCopyErrorType.OPEN_OUTPUT_STREAM_ERROR, progress.error!!.type)
    }

    @Test
    fun errorWriteToOutput() {
        val fakeContentResolver = FakeContentResolver()

        fakeContentResolver.outputStream = FailingOutputStream()

        val streamCopy = StreamCopy(fakeContentResolver)
        streamCopy.copy(
            Uri.parse("test://source"),
            Uri.parse("test://destination"),
        )

        val progress = streamCopy.progress.value

        assertEquals(true, progress.isFinished)
        assertEquals(true, progress.isFailed)
        assertEquals(StreamCopyErrorType.COPY_ERROR, progress.error!!.type)
    }
}
