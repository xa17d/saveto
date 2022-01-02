package at.xa1.saveto

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import at.xa1.saveto.android.IntentManager
import at.xa1.saveto.ui.Loading
import at.xa1.saveto.ui.Splash
import at.xa1.saveto.ui.theme.SaveToTheme

class MainActivity : ComponentActivity() {

    var intentManager: IntentManager = IntentManager() // TODO inject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intentManager.attach(this)
        setContent {
            SaveToTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Loading(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        intentManager.onActivityResult(requestCode, resultCode, data)
    }
}
