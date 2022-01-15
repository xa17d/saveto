package at.xa1.saveto.model

import android.content.Intent
import android.net.Uri
import android.os.Parcelable

data class Source(
    val type: Mime,
    val data: SourceData,
    val subject: String,
    val rawIntent: Intent
)

@JvmInline
value class Mime(val value: String) {
    companion object {
        val ANY: Mime get() = Mime("*/*")

        fun from(input: String?): Mime {
            if (input.isNullOrBlank()) {
                return ANY
            }

            return Mime(input)
        }
    }
}

sealed class SourceData {
    object Unknown : SourceData()
    data class Uri(val value: android.net.Uri) : SourceData()
    data class Text(val value: String) : SourceData()
}

fun intentToSource(intent: Intent): Source {
    return Source(
        type = Mime.from(intent.type),
        data = sourceDataFromIntent(intent),
        subject = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: "",
        rawIntent = intent
    )
}

fun sourceDataFromIntent(intent: Intent): SourceData {
    val streamUri = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri
    if (streamUri != null) {
        return SourceData.Uri(streamUri)
    }

    val intentData = intent.data
    if (intentData != null) {
        return SourceData.Uri(intentData)
    }

    val extraText = intent.getStringExtra(Intent.EXTRA_TEXT)
    if (extraText != null) {
        return SourceData.Text(extraText)
    }

    return SourceData.Unknown
}

fun Source.proposedFilename(): String {
    val baseName = subject
        .trim()
        .replace(" ", "_")
        .replace(Regex("[^a-zA-Z0-9\\-_]"), "-")
        .take(60)
        .ifEmpty { "file" } // todo localize?

    return baseName + extensionByMime(type)
}
