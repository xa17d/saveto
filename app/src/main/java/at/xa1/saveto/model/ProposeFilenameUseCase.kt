package at.xa1.saveto.model

import android.net.Uri
import at.xa1.saveto.feature.save.ContentResolverWrapper

class ProposeFilenameUseCase(
    private val contentResolver: ContentResolverWrapper,
    private val default: String
) {
    fun getFilenameFor(source: Source): String {
        val baseName = getBaseNameForSource(source)
        return addExtensionIfMissing(baseName, extensionByMime(source.type))
    }

    private fun addExtensionIfMissing(baseName: String, extension: String): String {
        return if (baseName.endsWith(extension, ignoreCase = true)) {
            baseName
        } else {
            baseName + extension
        }
    }

    private fun getBaseNameForSource(source: Source): String {
        return when (val data = source.data) {
            SourceData.Unknown -> getFilenameFromSubjectOrNull(source)
                ?: default

            is SourceData.Uri -> getFilenameFromUriOrNull(data.value)
                ?: getFilenameFromSubjectOrNull(source)
                ?: default

            is SourceData.Text -> getFilenameFromSubjectOrNull(source)
                ?: data.value.replaceInvalidCharsAndLimitLength()
        }
    }

    private fun getFilenameFromSubjectOrNull(source: Source): String? {
        if (source.subject.isNotEmpty()) {
            return source.subject.replaceInvalidCharsAndLimitLength()
        }
        return null
    }

    private fun getFilenameFromUriOrNull(uri: Uri): String? {
        val filenameFromContentResolver = contentResolver.getFilenameByContentUriOrNull(uri)
        if (filenameFromContentResolver != null) {
            return filenameFromContentResolver
        }

        return uri.lastPathSegment?.replaceInvalidCharsAndLimitLength()
    }

    private fun String.replaceInvalidCharsAndLimitLength(): String =
        trim()
            .replace(Regex("[^a-zA-Z0-9\\-_. ]"), "-")
            .take(30)
}
