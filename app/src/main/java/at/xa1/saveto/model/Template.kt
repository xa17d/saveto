package at.xa1.saveto.model

import androidx.annotation.StringRes
import at.xa1.saveto.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Template(
    val id: TemplateId = TemplateId.new(),
    val name: String = "",
    val suggestedFilename: String = TemplatePlaceholder.ORIGINAL_FILENAME.replacementPattern,
    val addExtensionIfMissing: Boolean = true
)

@JvmInline
value class TemplateId(private val value: String) {

    override fun toString(): String = value

    companion object {
        fun new(): TemplateId = TemplateId(UUID.randomUUID().toString())
    }
}

enum class TemplatePlaceholder(
    val id: String,
    @StringRes val description: Int,
    val value: (TemplatePlaceholderContext) -> String
) {
    YYYY("YYYY", R.string.templatePlaceHolderDescriptionYYYY, { context ->
        context.time.format("y")
    }),
    YY("YY", R.string.templatePlaceHolderDescriptionYY, { context ->
        context.time.format("yy")
    }),
    MMMM("MMMM", R.string.templatePlaceHolderDescriptionMMMM, { context ->
        context.time.format("MMMM")
    }),
    MMM("MMM", R.string.templatePlaceHolderDescriptionMMM, { context ->
        context.time.format("MMM")
    }),
    MM("MM", R.string.templatePlaceHolderDescriptionMM, { context ->
        context.time.format("MM")
    }),
    M("M", R.string.templatePlaceHolderDescriptionM, { context ->
        context.time.format("M")
    }),
    DD("DD", R.string.templatePlaceHolderDescriptionDD, { context ->
        context.time.format("dd")
    }),
    D("D", R.string.templatePlaceHolderDescriptionD, { context ->
        context.time.format("d")
    }),
    DAY_OF_WEEK("DAY_OF_WEEK", R.string.templatePlaceHolderDescriptionDAY_OF_WEEK, { context ->
        context.time.format("eee")
    }),
    DAY_OF_WEEK_FULL("DAY_OF_WEEK_FULL", R.string.templatePlaceHolderDescriptionDAY_OF_WEEK_FULL, { context ->
        context.time.format("eeee")
    }),
    HH("HH", R.string.templatePlaceHolderDescriptionHH, { context ->
        context.time.format("HH")
    }),
    H("H", R.string.templatePlaceHolderDescriptionH, { context ->
        context.time.format("H")
    }),
    LOWERCASE_HH("hh", R.string.templatePlaceHolderDescriptionhh, { context ->
        context.time.format("hh")
    }),
    LOWERCASE_H("h", R.string.templatePlaceHolderDescriptionh, { context ->
        context.time.format("h")
    }),
    AM_PM("AMPM", R.string.templatePlaceHolderDescriptionAMPM, { context ->
        context.time.format("a")
    }),
    LOWERCASE_MM("mm", R.string.templatePlaceHolderDescriptionmm, { context ->
        context.time.format("mm")
    }),
    LOWERCASE_M("m", R.string.templatePlaceHolderDescriptionm, { context ->
        context.time.format("m")
    }),
    LOWERCASE_SS("ss", R.string.templatePlaceHolderDescriptionss, { context ->
        context.time.format("ss")
    }),
    LOWERCASE_S("s", R.string.templatePlaceHolderDescriptions, { context ->
        context.time.format("s")
    }),
    ORIGINAL_FILENAME("ORIGINAL_FILENAME", R.string.templatePlaceHolderDescriptionORIGINAL_FILENAME, { context ->
        context.originalFilename
    });

    companion object {
        fun fill(pattern: String, context: TemplatePlaceholderContext): String =
            pattern.replace(Regex("\\{([A-Za-z0-9_-]+)\\}")) { match ->
                val id = match.groupValues[1]
                byId(id)?.value?.invoke(context) ?: match.value
            }

        private fun byId(id: String): TemplatePlaceholder? =
            entries.find { it.id == id }
    }
}

val TemplatePlaceholder.replacementPattern: String
    get() = "{$id}"

data class TemplatePlaceholderContext(
    val time: ZonedDateTime,
    val originalFilename: String
)

fun ZonedDateTime.format(pattern: String): String = DateTimeFormatter.ofPattern(pattern).format(this)
