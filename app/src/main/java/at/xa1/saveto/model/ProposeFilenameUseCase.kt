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
        if (source.subject.isNotEmpty()) {
            return source.subject.replaceInvalidCharsAndLimitLength()
        }

        return when (val data = source.data) {
            SourceData.Unknown -> default
            is SourceData.Uri -> getFilenameFromUri(data.value)
            is SourceData.Text -> data.value.replaceInvalidCharsAndLimitLength()
        }
    }

    private fun getFilenameFromUri(uri: Uri): String {
        val filenameFromContentResolver = contentResolver.getFilenameByContentUriOrNull(uri)
        if (filenameFromContentResolver != null) {
            return filenameFromContentResolver
        }

        return uri.lastPathSegment?.replaceInvalidCharsAndLimitLength() ?: default
    }

    private fun String.replaceInvalidCharsAndLimitLength(): String =
        trim()
            .replace(Regex("[^a-zA-Z0-9\\-_. ]"), "-")
            .take(30)
}
