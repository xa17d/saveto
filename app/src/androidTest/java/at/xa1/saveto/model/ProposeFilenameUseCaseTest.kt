package at.xa1.saveto.model

import android.content.Intent
import android.net.Uri
import at.xa1.saveto.feature.save.FakeContentResolver
import org.junit.Assert.assertEquals
import org.junit.Test

class ProposeFilenameUseCaseTest {
    private val source = Source(
        type = Mime.from("text/plain"),
        data = SourceData.Unknown,
        subject = "",
        rawIntent = Intent()
    )

    private val contentResolver = FakeContentResolver()

    private val instance = ProposeFilenameUseCase(contentResolver, "default")

    @Test
    fun subjectIsUsedByDefault() {
        val result = instance.getFilenameFor(
            source.copy(subject = "TEST SUBJECT")
        )

        assertEquals(
            "TEST SUBJECT.txt",
            result
        )
    }

    @Test
    fun defaultIsUsedForUnknownSourceWithoutSubject() {
        val result = instance.getFilenameFor(
            source.copy(data = SourceData.Unknown)
        )

        assertEquals(
            "default.txt",
            result
        )
    }

    @Test
    fun textIsUsedIfTextSourceAndNoSubject() {
        val result = instance.getFilenameFor(
            source.copy(data = SourceData.Text("Example Text"))
        )

        assertEquals(
            "Example Text.txt",
            result
        )
    }

    @Test
    fun shortenedTextIsUsedIfTextSourceAndNoSubject() {
        val result = instance.getFilenameFor(
            source.copy(data = SourceData.Text("This is an long example test-text"))
        )

        assertEquals(
            "This is an long example test-t.txt",
            result
        )
    }

    @Test
    fun lastSegmentIsUsedIfUriSourceAndNoSubject() {
        val result = instance.getFilenameFor(
            source.copy(
                type = Mime.from("image/*"),
                data = SourceData.Uri(Uri.parse("content://media/external/images/media/14"))
            )
        )

        assertEquals(
            "14.jpg",
            result
        )
    }

    @Test
    fun lastSegmentIsUsedIfUriSourceAndNoSubject2() {
        val result = instance.getFilenameFor(
            source.copy(
                type = Mime.from("image/jpeg"),
                data = SourceData.Uri(
                    Uri.parse("content://com.android.providers.media.documents/document/image%3A14")
                )
            )
        )

        assertEquals(
            "image-14.jpg",
            result
        )
    }

    @Test
    fun defaultIsUsedIfUriSourceNoSubjectAndNoLastSegment() {
        val result = instance.getFilenameFor(
            source.copy(
                data = SourceData.Uri(
                    Uri.parse("content://provider.without.path")
                )
            )
        )

        assertEquals(
            "default.txt",
            result
        )
    }

    @Test
    fun subjectIsPreferredOverLastSegmentOfUriSource() {
        val result = instance.getFilenameFor(
            source.copy(
                subject = "some-subject",
                data = SourceData.Uri(
                    Uri.parse("content://some.provider/file.txt")
                )
            )
        )

        assertEquals(
            "some-subject.txt",
            result
        )
    }

    @Test
    fun contentProviderNameIsUsedIfUriSourceAndNoSubject() {
        contentResolver.filenameForUri = "contentProviderFile"
        val result = instance.getFilenameFor(
            source.copy(
                data = SourceData.Uri(
                    Uri.parse("content://some.provider/file.txt")
                )
            )
        )

        assertEquals(
            "contentProviderFile.txt",
            result
        )
    }

    @Test
    fun subjectIsPreferredOverContentProviderName() {
        contentResolver.filenameForUri = "contentProviderFile"
        val result = instance.getFilenameFor(
            source.copy(
                subject = "some-subject",
                data = SourceData.Uri(
                    Uri.parse("content://some.provider/file.txt")
                )
            )
        )

        assertEquals(
            "some-subject.txt",
            result
        )
    }

    @Test
    fun googleRecorder() {
        val result = instance.getFilenameFor(
            source.copy(
                type = Mime.from("audio/mp4a-latm"),
                data = SourceData.Uri(
                    Uri.parse(
                        "content://com.google.android.apps.recorder.fileprovider/share/recordings/" +
                            "053e9918-8ac4-4da1-9d4a-7937a70e5e14/Video%20-%20Sunday%20at%2017-02.m4a"
                    )
                )
            )
        )

        assertEquals(
            "Video - Sunday at 17-02.m4a",
            result
        )
    }
}
