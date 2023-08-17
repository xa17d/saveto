package at.xa1.saveto.model.template

import android.content.SharedPreferences
import at.xa1.saveto.R
import at.xa1.saveto.common.android.Resources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedPreferencesTemplates(
    private val sharedPreferences: SharedPreferences,
    private val resources: Resources,
    private val saveScope: CoroutineScope
) {
    private var persistedJson: String?
        get() = sharedPreferences.getString("filenameTemplates", null)
        set(value) = sharedPreferences.edit().putString("filenameTemplates", value).apply()

    private fun restore(): List<Template> {
        val json = persistedJson
            ?: return listOf(
                Template(
                    id = TemplateId("9396121f-2c98-4436-a98f-0e67a18f107f"),
                    name = resources.string(R.string.settingsTemplateDefaultName),
                    suggestedFilename = TemplatePlaceholder.ORIGINAL_FILENAME.replacementPattern,
                    addExtensionIfMissing = true
                )
            )

        return Json.decodeFromString<List<Template>>(json)
    }

    private fun store(value: List<Template>) {
        val json = Json.encodeToString(value)
        persistedJson = json
    }

    val templates: Templates = Templates(restore())

    init {
        saveScope.launch {
            templates.all.collect { value ->
                store(value)
            }
        }
    }
}
