package at.xa1.saveto

import android.content.Intent
import at.xa1.saveto.feature.save.SaveArgs
import at.xa1.saveto.feature.splash.SplashDestination
import at.xa1.saveto.model.intentToSource
import at.xa1.saveto.navigation.Coordinator
import at.xa1.saveto.navigation.Destination

/**
 * Main coordinator that is started on app start up.
 */
class MainCoordinator(
    private val saveFlow: Destination<SaveArgs>,
    private val launchedFlow: Destination<LaunchedArgs>
) : Coordinator<MainArgs>() {

    override fun onStart() {
        super.onStart()

        navigator.goTo(SplashDestination)

        when (args.intent.action) {
            Intent.ACTION_MAIN -> launched()
            Intent.ACTION_SEND, Intent.ACTION_VIEW -> saveFlow()
            else -> launched() // TODO report somehow?
        }
    }

    private fun launched() {
        navigator.goTo(launchedFlow) {
            LaunchedArgs(
                onClose = args.onClose
            )
        }
    }

    private fun saveFlow() {
        navigator.goTo(saveFlow) {
            SaveArgs(
                source = intentToSource(args.intent),
                onClose = args.onClose
            )
        }
    }
}

data class MainArgs(
    val intent: Intent,
    val onClose: (result: MainResult) -> Unit
)

enum class MainResult {
    OK,
    ABORT
}
