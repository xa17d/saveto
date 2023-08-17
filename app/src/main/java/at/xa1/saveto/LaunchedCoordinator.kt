package at.xa1.saveto

import at.xa1.saveto.common.navigation.Coordinator
import at.xa1.saveto.common.navigation.Destination
import at.xa1.saveto.feature.intro.IntroArgs
import at.xa1.saveto.feature.intro.IntroDestination
import at.xa1.saveto.feature.settings.SettingsCoordinatorArgs
import at.xa1.saveto.model.SettingsStore

/**
 * Coordinator that is started when the app is launched from the Launcher.
 */
class LaunchedCoordinator(
    private val settingsStore: SettingsStore,
    private val settingsCoordinator: Destination<SettingsCoordinatorArgs>
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
                onSettings = {
                    settingsStore.introSeen = true
                    settings()
                },
                onClose = {
                    settingsStore.introSeen = true
                    close()
                }
            )
        }
    }

    private fun settings() {
        navigator.goTo(settingsCoordinator) {
            SettingsCoordinatorArgs(
                onIntro = ::intro,
                onClose = ::close
            )
        }
    }

    private fun close() {
        args.onClose(MainResult.OK)
    }
}

data class LaunchedArgs(
    val onClose: (result: MainResult) -> Unit
)
