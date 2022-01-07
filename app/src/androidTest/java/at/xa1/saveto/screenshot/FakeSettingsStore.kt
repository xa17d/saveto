package at.xa1.saveto.screenshot

import at.xa1.saveto.model.PreviewMode
import at.xa1.saveto.model.SettingsStore

class FakeSettingsStore : SettingsStore {
    override var previewMode: PreviewMode = PreviewMode.NONE
    override var introSeen: Boolean = true
}
