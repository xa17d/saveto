package at.xa1.saveto.model

import at.xa1.saveto.model.template.Template
import at.xa1.saveto.model.template.TemplateId
import at.xa1.saveto.model.template.Templates

class FakeSettingsStore : SettingsStore {
    override var previewMode: PreviewMode = PreviewMode.NONE
    override var introSeen: Boolean = true
    override var version: String = "TEST-VERSION"
    override val templates: Templates = Templates(
        listOf(
            Template(
                id = TemplateId.new(),
                name = "Default",
                suggestedFilename = "{ORIGINAL_FILENAME}",
                addExtensionIfMissing = true
            ),
            Template(
                id = TemplateId.new(),
                name = "Custom",
                suggestedFilename = "{YYYY}-{MM}-{DD}_{ORIGINAL_FILENAME}",
                addExtensionIfMissing = true
            )
        )
    )
}
