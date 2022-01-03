package at.xa1.saveto.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.xa1.saveto.model.PreviewMode
import at.xa1.saveto.model.SettingsStore
import at.xa1.saveto.navigation.Destination

@Composable
fun Settings(modifier: Modifier = Modifier, args: SettingsArgs) {
    Column {
        Button(onClick = { args.onIntro() }) {
            Text(text = "Intro")
        }

        val previewMode = remember { mutableStateOf(args.settingsStore.previewMode) }
        Text("Preview")
        Column {
            Row {
                RadioButton(selected = previewMode.value == PreviewMode.NONE, onClick = {
                    previewMode.value = PreviewMode.NONE
                    args.settingsStore.previewMode = PreviewMode.NONE
                })
                Spacer(modifier = Modifier.size(16.dp))
                Text("NONE")
            }

            Row {
                RadioButton(selected = previewMode.value == PreviewMode.INTENT_DETAILS, onClick = {
                    previewMode.value = PreviewMode.INTENT_DETAILS
                    args.settingsStore.previewMode = PreviewMode.INTENT_DETAILS
                })
                Spacer(modifier = Modifier.size(16.dp))
                Text("INTENT_DETAILS")
            }

        }
    }
}

val SettingsDestination = Destination<SettingsArgs> {
    Settings(modifier = Modifier.fillMaxSize(), args)
}

data class SettingsArgs(
    val settingsStore: SettingsStore,
    val onIntro: () -> Unit
)
