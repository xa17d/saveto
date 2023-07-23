package at.xa1.saveto.screenshot

import android.graphics.Bitmap
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
import androidx.lifecycle.lifecycleScope
import androidx.test.runner.screenshot.Screenshot
import at.xa1.saveto.R
import at.xa1.saveto.common.android.AndroidResources
import at.xa1.saveto.common.android.IntentManager
import at.xa1.saveto.common.android.Resources
import at.xa1.saveto.common.android.ShareChooser
import at.xa1.saveto.common.navigation.HostHolder
import at.xa1.saveto.feature.save.Loading
import at.xa1.saveto.feature.save.SaveDialog
import at.xa1.saveto.feature.save.Success
import at.xa1.saveto.feature.save.progressString
import at.xa1.saveto.feature.settings.Settings
import at.xa1.saveto.feature.settings.SettingsArgs
import at.xa1.saveto.feature.splash.Splash
import at.xa1.saveto.model.Mime
import at.xa1.saveto.ui.theme.SaveToTheme
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

        takeScreenshot()
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
                        contentDescription = "SaveTo logo"
                    )

                    Spacer(modifier = Modifier.fillMaxHeight())
                }
            }
        }

        val activity = composeTestRule.activity
        activity.runOnUiThread {
            ShareChooser(activity).open()
        }

        takeScreenshot()
    }

    @Test
    fun saveDialog() {
        val activity = composeTestRule.activity
        activity.runOnUiThread {
            val hostHolder = HostHolder()
            hostHolder.attach(activity)
            activity.lifecycleScope.launch {
                SaveDialog(IntentManager(hostHolder)).show(Mime.from("image/png"), "my_image.png")
            }
        }

        takeScreenshot()
    }

    @Test
    fun loading() {
        composeTestRule.mainClock.autoAdvance = false

        val resources: Resources = AndroidResources(composeTestRule.activity.applicationContext)

        composeTestRule.setContent {
            SaveToTheme {
                Loading(
                    modifier = Modifier.fillMaxSize(),
                    text = MutableStateFlow(resources.progressString((12.3 * 1024 * 1024).toLong()))
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(800)

        takeScreenshot()
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

        takeScreenshot()
    }

    @Test
    fun success() {
        composeTestRule.setContent {
            SaveToTheme {
                Success(modifier = Modifier.fillMaxSize())
            }
        }

        takeScreenshot()
    }

    private fun takeScreenshot() {
        // Give UI some time to settle before taking the screenshot.
        Thread.sleep(1000)

        val screenCapture = Screenshot.capture()
        screenCapture.name = getCallingMethodName()
        screenCapture.format = Bitmap.CompressFormat.PNG
        screenCapture.process()
    }

    private fun getCallingMethodName(parents: Int = 1): String {
        val stackTrace = Exception().stackTrace
        val stackTraceIndex = parents + 2
        return if (stackTraceIndex >= stackTrace.size) {
            "unknown-overflow"
        } else {
            stackTrace[stackTraceIndex].methodName
        }
    }
}
