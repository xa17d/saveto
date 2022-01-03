package at.xa1.saveto.ui

import android.content.Intent
import android.net.Uri
import at.xa1.saveto.MainResult
import at.xa1.saveto.android.HostHolder
import at.xa1.saveto.model.SettingsStore
import at.xa1.saveto.navigation.Coordinator
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class LaunchedCoordinator(
    private val settingsStore: SettingsStore,
    private val hostHolder: HostHolder
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
                onClose = ::close
            )
        }
    }

    private fun settings() {
        navigator.goTo(SettingsDestination) {
            SettingsArgs(
                settingsStore = settingsStore,
                onOssLicenses = {
                    hostHolder.runOrEnqueue {
                        activity.startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
                    }
                },
                onIntro = ::intro,
                onClose = ::close,
                onContact = {
                    hostHolder.runOrEnqueue {
                        activity.startActivity(
                            Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://xa1.at/saveto/contact")
                            }
                        )
                    }
                }
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
