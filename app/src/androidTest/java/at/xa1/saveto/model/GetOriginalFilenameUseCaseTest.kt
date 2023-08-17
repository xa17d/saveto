package at.xa1.saveto.model

import android.content.Intent
import android.net.Uri
import at.xa1.saveto.feature.save.FakeContentResolver
import org.junit.Assert.assertEquals
import org.junit.Test

class GetOriginalFilenameUseCaseTest {
    private val source = Source(
        type = Mime.from("text/plain"),
        data = SourceData.Unknown,
        subject = "",
        rawIntent = Intent()
    )

    private val contentResolver = FakeContentResolver()

    private val instance = GetOriginalFilenameUseCase(contentResolver, "default")

    @Test
    fun unknownSource_subjectIsUsedByDefault() {
        val result = instance.getOriginalFilenameFrom(
            source.copy(
                data = SourceData.Unknown,
                subject = "TEST SUBJECT"
            )
        )

        assertEquals(
            "TEST SUBJECT",
            result
        )
    }

    @Test
    fun unknownSource_defaultIsUsedWhenSubjectEmpty() {
        val result = instance.getOriginalFilenameFrom(
            source.copy(data = SourceData.Unknown)
        )

        assertEquals(
            "default",
            result
        )
    }

    @Test
    fun textSource_subjectIsUsedByDefault() {
        val result = instance.getOriginalFilenameFrom(
            source.copy(
                data = SourceData.Text("Example Text"),
                subject = "TEST SUBJECT"
            )
        )

        assertEquals(
            "TEST SUBJECT",
            result
        )
    }

    @Test
    fun textSource_textIsUsedWhenSubjectEmpty() {
        val result = instance.getOriginalFilenameFrom(
            source.copy(data = SourceData.Text("Example Text"))
        )

        assertEquals(
            "Example Text",
            result
        )
    }

    @Test
    fun uriSource_lastSegmentIsUsedWhenContentCannotBeResolved() {
        val result = instance.getOriginalFilenameFrom(
            source.copy(
                type = Mime.from("image/*"),
                data = SourceData.Uri(Uri.parse("content://media/external/images/media/14"))
            )
        )

        assertEquals(
            "14",
            result
        )
    }

    @Test
    fun uriSource_lastSegmentIsUsedWhenContentCannotBeResolved_2() {
        val result = instance.getOriginalFilenameFrom(
            source.copy(
                type = Mime.from("image/jpeg"),
                data = SourceData.Uri(
                    Uri.parse("content://com.android.providers.media.documents/document/image%3A14")
                )
            )
        )

        assertEquals(
            "image:14",
            result
        )
    }

    @Test
    fun uriSource_defaultIsUsedWhenContentCannotBeResolvedAndNoLastSegmentAndNoSubject() {
        val result = instance.getOriginalFilenameFrom(
            source.copy(
                data = SourceData.Uri(
                    Uri.parse("content://provider.without.path")
                )
            )
        )

        assertEquals(
            "default",
            result
        )
    }

    @Test
    fun uriSource_lastSegmentOfUriIsPreferredOverSubject() {
        val result = instance.getOriginalFilenameFrom(
            source.copy(
                subject = "some-subject",
                data = SourceData.Uri(
                    Uri.parse("content://some.provider/file.txt")
                )
            )
        )

        assertEquals(
            "file.txt",
            result
        )
    }

    @Test
    fun uriSource_resolvedContentNameIsPreferredOverSubject() {
        contentResolver.filenameForUri = "contentProviderFile"
        val result = instance.getOriginalFilenameFrom(
            source.copy(
                subject = "some-subject",
                data = SourceData.Uri(
                    Uri.parse("content://some.provider/file.txt")
                )
            )
        )

        assertEquals(
            "contentProviderFile",
            result
        )
    }

    @Test
    fun uriSource_subjectIsUsedWhenContentCannotBeResolvedAndNoLastSegment() {
        val result = instance.getOriginalFilenameFrom(
            source.copy(
                subject = "some-subject",
                data = SourceData.Uri(
                    Uri.parse("content://provider.without.path")
                )
            )
        )

        assertEquals(
            "some-subject",
            result
        )
    }

    @Test
    fun uriSource_googleRecorder() {
        val result = instance.getOriginalFilenameFrom(
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
