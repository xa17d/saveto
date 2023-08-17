package at.xa1.saveto.model

import android.net.Uri
import at.xa1.saveto.feature.save.ContentResolverWrapper

class GetOriginalFilenameUseCase(
    private val contentResolver: ContentResolverWrapper,
    private val default: String
) {
    fun getOriginalFilenameFrom(source: Source): String {
        return when (val data = source.data) {
            SourceData.Unknown -> getFilenameFromSubjectOrNull(source)
                ?: default

            is SourceData.Uri -> getFilenameFromUriOrNull(data.value)
                ?: getFilenameFromSubjectOrNull(source)
                ?: default

            is SourceData.Text -> getFilenameFromSubjectOrNull(source)
                ?: data.value
        }
    }

    private fun getFilenameFromSubjectOrNull(source: Source): String? {
        if (source.subject.isNotEmpty()) {
            return source.subject
        }
        return null
    }

    private fun getFilenameFromUriOrNull(uri: Uri): String? {
        val filenameFromContentResolver = contentResolver.getFilenameByContentUriOrNull(uri)
        if (filenameFromContentResolver != null) {
            return filenameFromContentResolver
        }

        return uri.lastPathSegment
    }
}
