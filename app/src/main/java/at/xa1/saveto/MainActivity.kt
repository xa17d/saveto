package at.xa1.saveto

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import at.xa1.saveto.android.HostHolder
import at.xa1.saveto.android.IntentManager
import at.xa1.saveto.android.SaveDialog
import at.xa1.saveto.android.StreamCopy
import at.xa1.saveto.navigation.CDestination
import at.xa1.saveto.navigation.ComposeNavigator
import at.xa1.saveto.ui.SaveArgs
import at.xa1.saveto.ui.SaveCoordinator
import at.xa1.saveto.ui.Source
import at.xa1.saveto.ui.theme.SaveToTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {

    var hostHolder = HostHolder() // TODO inject
    var intentManager: IntentManager = IntentManager(hostHolder) // TODO inject
    var nav = ComposeNavigator().apply {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nav.apply {
            val d = CDestination {
                SaveCoordinator(
                    CoroutineScope(Dispatchers.Main),
                    SaveDialog(intentManager),
                    StreamCopy(contentResolver)
                )
            }

            goTo(d) {
                SaveArgs(
                    source = Source(intent, intent.type ?: "*/*"),
                    onClose = { success->
                        hostHolder.runOrEnqueue {
                            activity.setResult(
                                if (success) {
                                    Activity.RESULT_OK
                                } else {
                                    Activity.RESULT_CANCELED
                                }
                            )
                            activity.finish()
                        }
                    }
                )
            }
        }

        hostHolder.attach(this)
        setContent {
            SaveToTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    nav.show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        intentManager.onActivityResult(requestCode, resultCode, data)
    }
}
