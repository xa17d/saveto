package at.xa1.saveto.model

import android.content.SharedPreferences

interface SettingsStore {
    var previewMode: PreviewMode
    var introSeen: Boolean
}

class SharedPreferencesSettingsStore(
    private val sharedPreferences: SharedPreferences
) : SettingsStore {
    override var previewMode: PreviewMode
        get() {
            return when (sharedPreferences.getString("previewMode", "NONE")) {
                "NONE" -> PreviewMode.NONE
                "INTENT_DETAILS" -> PreviewMode.INTENT_DETAILS
                else -> error("invalid shared preference value")
            }
        }
        set(value) {
            val s = when (value) {
                PreviewMode.NONE -> "NONE"
                PreviewMode.INTENT_DETAILS -> "INTENT_DETAILS"
            }
            sharedPreferences.edit().putString("previewMode", s).apply()
        }

    override var introSeen: Boolean
        get() = sharedPreferences.getBoolean("introSeen", false)
        set(value) = sharedPreferences.edit().putBoolean("introSeen", value).apply()
}

enum class PreviewMode {
    NONE,
    INTENT_DETAILS
}
