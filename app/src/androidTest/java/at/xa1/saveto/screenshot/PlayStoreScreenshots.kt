package at.xa1.saveto.screenshot

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import at.xa1.saveto.R
import at.xa1.saveto.android.HostHolder
import at.xa1.saveto.android.IntentManager
import at.xa1.saveto.android.SaveDialog
import at.xa1.saveto.android.ShareChooser
import at.xa1.saveto.model.Mime
import at.xa1.saveto.ui.Loading
import at.xa1.saveto.ui.Settings
import at.xa1.saveto.ui.SettingsArgs
import at.xa1.saveto.ui.Splash
import at.xa1.saveto.ui.Success
import at.xa1.saveto.ui.theme.SaveToTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test

class PlayStoreScreenshots {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    // use createComposeRule()  if you don't need access to an activity

    @Test
    fun splash() {

        composeTestRule.setContent {
            SaveToTheme {
                Splash(modifier = Modifier.fillMaxSize())
            }
        }

        waitForScreenshot()
    }

    @Test
    fun shareMenu() {
        // Start the app
        composeTestRule.setContent {
            SaveToTheme {
                Column(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopAppBar(
                        title = { Text(text = "Some App") },
                        navigationIcon = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Filled.Close, "close")
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Filled.Share, "share")
                            }
                        }
                    )

                    Icon(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .aspectRatio(1f),
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        tint = Color.Gray,
                        contentDescription = "SaveTo logo",
                    )

                    Spacer(modifier = Modifier.fillMaxHeight())
                }
            }
        }

        val activity = composeTestRule.activity
        activity.runOnUiThread {
            ShareChooser(activity).open()
        }

        waitForScreenshot()
    }

    @Test
    fun saveDialog() {
        val activity = composeTestRule.activity
        activity.runOnUiThread {
            val hostHolder = HostHolder()
            hostHolder.attach(activity)
            GlobalScope.launch { // TODO don't use globalscope
                SaveDialog(IntentManager(hostHolder)).show(Mime.from("image/png"), "my_image.png")
            }
        }

        waitForScreenshot()
    }

    @Test
    fun loading() {

        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.setContent {
            SaveToTheme {
                Loading(
                    modifier = Modifier.fillMaxSize(),
                    text = MutableStateFlow("13.2 MiB written...")
                ) // TODO use same resource as real deal
            }
        }

        composeTestRule.mainClock.advanceTimeBy(800)

        waitForScreenshot()
    }

    @Test
    fun settings() {
        composeTestRule.setContent {
            SaveToTheme {
                Settings(
                    modifier = Modifier.fillMaxSize(),
                    args = SettingsArgs(
                        settingsStore = FakeSettingsStore(),
                        onOssLicenses = {},
                        onIntro = {},
                        onClose = {},
                        onContact = {}
                    )
                )
            }
        }

        waitForScreenshot()
    }

    @Test
    fun success() {
        composeTestRule.setContent {
            SaveToTheme {
                Success(modifier = Modifier.fillMaxSize())
            }
        }

        waitForScreenshot()
    }

    private fun waitForScreenshot() {
        Thread.sleep(5000)
    }
}
