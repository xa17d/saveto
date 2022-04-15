package at.xa1.saveto.model

import android.content.Intent
import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Test

class ProposedFilenameTest {

    private val source = Source(
        type = Mime.from("text/plain"),
        data = SourceData.Unknown,
        subject = "",
        rawIntent = Intent()
    )

    @Test
    fun subjectIsUsedByDefault() {
        val result = source
            .copy(subject = "TEST SUBJECT")
            .proposedFilename()

        assertEquals(
            "TEST_SUBJECT.txt",
            result
        )
    }

    @Test
    fun noSubjectUnknownSource_DefaultIsUsed() {
        val result = source
            .copy(data = SourceData.Unknown)
            .proposedFilename()

        assertEquals(
            "file.txt",
            result
        )
    }

    @Test
    fun noSubjectTextSource_ShortenedTextIsUsed() {
        val result = source
            .copy(data = SourceData.Text("This is an long example test text"))
            .proposedFilename()

        assertEquals(
            "This_is_an_long_ex.txt",
            result
        )
    }

    @Test
    fun noSubjectTextSource_TextIsUsed() {
        val result = source
            .copy(data = SourceData.Text("Example Text"))
            .proposedFilename()

        assertEquals(
            "Example_Text.txt",
            result
        )
    }

    @Test
    fun noSubjectUriSource_LastSegmentIsUsed() {
        val result = source
            .copy(
                type = Mime.from("image/*"),
                data = SourceData.Uri(Uri.parse("content://media/external/images/media/14"))
            )
            .proposedFilename()

        assertEquals(
            "14.jpg",
            result
        )
    }

    @Test
    fun noSubjectUriSource_LastSegmentIsUsed2() {
        val result = source
            .copy(
                type = Mime.from("image/jpeg"),
                data = SourceData.Uri(
                    Uri.parse("content://com.android.providers.media.documents/document/image%3A14")
                )
            )
            .proposedFilename()

        assertEquals(
            "image-14.jpg",
            result
        )
    }

    @Test
    fun noSubjectUriSource_DefaultIsUsedIfNoLastSegment() {
        val result = source
            .copy(
                data = SourceData.Uri(
                    Uri.parse("content://provider.without.path")
                )
            )
            .proposedFilename("default")

        assertEquals(
            "default.txt",
            result
        )
    }

    @Test
    fun dataButSubject_SubjectIsUsed() {
        val result = source
            .copy(
                subject = "some-subject",
                data = SourceData.Uri(
                    Uri.parse("content://some.provider/file.txt")
                )
            )
            .proposedFilename()

        assertEquals(
            "some-subject.txt",
            result
        )
    }
}