package at.xa1.saveto.ui

import at.xa1.saveto.MainResult
import at.xa1.saveto.model.SettingsStore
import at.xa1.saveto.navigation.Coordinator

class LaunchedCoordinator(
    private val settingsStore: SettingsStore
) : Coordinator<LaunchedArgs>() {

    override fun onStart() {
        if (!settingsStore.introSeen) {
            intro()
        } else {
            settings()
        }
    }

    private fun intro() {
        navigator.goTo(IntroDestination) {
            IntroArgs(
                onCompleted = {
                    settingsStore.introSeen = true
                    settings()
                }
            )
        }
    }

    private fun settings() {
        navigator.goTo(SplashDestination)
    }
}

data class LaunchedArgs(
    val onClose: (result: MainResult) -> Unit
)
