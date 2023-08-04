package at.xa1.saveto.screenshot

import at.xa1.saveto.model.PreviewMode
import at.xa1.saveto.model.SettingsStore
import at.xa1.saveto.model.Template
import at.xa1.saveto.model.TemplateId
import at.xa1.saveto.model.Templates

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
            )
        )
    )
}
