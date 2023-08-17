package at.xa1.saveto.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.xa1.saveto.R
import at.xa1.saveto.android.compose.Scrollable
import at.xa1.saveto.common.TemplateList
import at.xa1.saveto.common.navigation.Destination
import at.xa1.saveto.model.FakeSettingsStore
import at.xa1.saveto.model.SettingsStore
import at.xa1.saveto.model.template.Template
import at.xa1.saveto.model.template.TemplateId
import at.xa1.saveto.ui.OptionButton

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
                SettingsCard(title = stringResource(id = R.string.settingsPreview)) {
                    PreviewSettings(settingsStore = args.settingsStore)
                }

                Spacer(modifier = Modifier.size(16.dp))

                val exampleContext = ExampleTemplatePlaceholderContext()

                SettingsCard(title = stringResource(id = R.string.settingsTemplates)) {
                    TemplateList(
                        templates = args.settingsStore.templates,
                        context = exampleContext,
                        allowEdit = true,
                        onAddTemplate = args.onAddTemplate,
                        onRemoveTemplate = args.onRemoveTemplate,
                        onSelectTemplate = args.onEditTemplate
                    )
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

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = stringResource(
                        id = R.string.settingsVersion,
                        args.settingsStore.version
                    ),
                    fontSize = 12.sp
                )
            }
        }
    }
}

val SettingsDestination = Destination<SettingsArgs> {
    Settings(modifier = Modifier.fillMaxSize(), args)
}

data class SettingsArgs(
    val settingsStore: SettingsStore,
    val onAddTemplate: () -> Unit,
    val onRemoveTemplate: (id: TemplateId) -> Unit,
    val onEditTemplate: (template: Template) -> Unit,
    val onOssLicenses: () -> Unit,
    val onIntro: () -> Unit,
    val onClose: () -> Unit,
    val onContact: () -> Unit
)

@Preview
@Composable
fun SettingsPreview() {
    Settings(
        args = SettingsArgs(
            settingsStore = FakeSettingsStore(),
            onAddTemplate = {},
            onRemoveTemplate = {},
            onEditTemplate = {},
            onOssLicenses = {},
            onIntro = {},
            onClose = {},
            onContact = {}
        )
    )
}
