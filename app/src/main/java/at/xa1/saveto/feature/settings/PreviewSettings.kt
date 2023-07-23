package at.xa1.saveto.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import at.xa1.saveto.R
import at.xa1.saveto.model.PreviewMode
import at.xa1.saveto.model.SettingsStore

@Composable
fun PreviewSettings(settingsStore: SettingsStore) {
    val previewMode = remember { mutableStateOf(settingsStore.previewMode) }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                previewMode.value = PreviewMode.NONE
                settingsStore.previewMode = PreviewMode.NONE
            }
    ) {
        RadioButton(
            selected = previewMode.value == PreviewMode.NONE,
            onClick = {
                previewMode.value = PreviewMode.NONE
                settingsStore.previewMode = PreviewMode.NONE
            }
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(stringResource(id = R.string.settingsPreviewModeNone))
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                previewMode.value = PreviewMode.INTENT_DETAILS
                settingsStore.previewMode = PreviewMode.INTENT_DETAILS
            }
    ) {
        RadioButton(
            selected = previewMode.value == PreviewMode.INTENT_DETAILS,
            onClick = {
                previewMode.value = PreviewMode.INTENT_DETAILS
                settingsStore.previewMode = PreviewMode.INTENT_DETAILS
            }
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(stringResource(id = R.string.settingsPreviewModeIntentDetails))
    }
}
