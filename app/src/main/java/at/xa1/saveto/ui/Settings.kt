package at.xa1.saveto.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import at.xa1.saveto.R
import at.xa1.saveto.android.Scrollable
import at.xa1.saveto.model.PreviewMode
import at.xa1.saveto.model.SettingsStore
import at.xa1.saveto.navigation.Destination

@Composable
fun Settings(modifier: Modifier = Modifier, args: SettingsArgs) {
    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.settingsTitle)) },
            navigationIcon = {
                IconButton(onClick = { args.onClose() }) {
                    Icon(Icons.Filled.Close, stringResource(id = R.string.settingsClose))
                }
            }
        )

        Scrollable(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Column {
                val previewMode = remember { mutableStateOf(args.settingsStore.previewMode) }

                Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            stringResource(id = R.string.settingsPreview),
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    previewMode.value = PreviewMode.NONE
                                    args.settingsStore.previewMode = PreviewMode.NONE
                                }
                        ) {
                            RadioButton(selected = previewMode.value == PreviewMode.NONE, onClick = {
                                previewMode.value = PreviewMode.NONE
                                args.settingsStore.previewMode = PreviewMode.NONE
                            })
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(stringResource(id = R.string.settingsPreviewModeNone))
                        }

                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    previewMode.value = PreviewMode.INTENT_DETAILS
                                    args.settingsStore.previewMode = PreviewMode.INTENT_DETAILS
                                },
                        ) {
                            RadioButton(selected = previewMode.value == PreviewMode.INTENT_DETAILS, onClick = {
                                previewMode.value = PreviewMode.INTENT_DETAILS
                                args.settingsStore.previewMode = PreviewMode.INTENT_DETAILS
                            })
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(stringResource(id = R.string.settingsPreviewModeIntentDetails))
                        }
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))
                OptionButton(
                    text = stringResource(id = R.string.settingsRestartIntro),
                    onClick = { args.onIntro() }
                )
                OptionButton(
                    text = stringResource(id = R.string.settingsContact),
                    onClick = { args.onContact() }
                )
                OptionButton(
                    text = stringResource(id = R.string.settingsOssLicenses),
                    onClick = { args.onOssLicenses() }
                )

            }
        }
    }
}

@Composable
fun OptionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .defaultMinSize(minHeight = 48.dp)
    ) {
        Text(text = text)
    }
}

val SettingsDestination = Destination<SettingsArgs> {
    Settings(modifier = Modifier.fillMaxSize(), args)
}

data class SettingsArgs(
    val settingsStore: SettingsStore,
    val onOssLicenses: () -> Unit,
    val onIntro: () -> Unit,
    val onClose: () -> Unit,
    val onContact: () -> Unit,
)
