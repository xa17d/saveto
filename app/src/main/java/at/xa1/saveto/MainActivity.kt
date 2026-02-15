package at.xa1.saveto

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import at.xa1.saveto.common.android.IntentManager
import at.xa1.saveto.common.navigation.ComposeNavigator
import at.xa1.saveto.common.navigation.HostHolder
import at.xa1.saveto.di.Inject
import at.xa1.saveto.di.getInjector
import at.xa1.saveto.ui.theme.SaveToTheme

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var hostHolder: HostHolder

    @Inject
    lateinit var intentManager: IntentManager

    @Inject
    lateinit var navigator: ComposeNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        getInjector().inject(this)
        hostHolder.attach(this)

        setContent {
            SaveToTheme {
                Surface(
                    modifier = Modifier.navigationBarsPadding(),
                    color = MaterialTheme.colors.background
                ) {
                    navigator.Show()
                }
            }
        }
    }

    @Deprecated("Use Activity Result API", ReplaceWith("registerForActivityResult()"))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        intentManager.onActivityResult(requestCode, resultCode, data)
    }
}
