package at.xa1.saveto.feature.save

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.xa1.saveto.R
import at.xa1.saveto.android.compose.Scrollable
import at.xa1.saveto.common.navigation.Destination
import at.xa1.saveto.feature.settings.CodeText
import at.xa1.saveto.feature.settings.ExampleTemplatePlaceholderContext
import at.xa1.saveto.model.FakeSettingsStore
import at.xa1.saveto.model.template.Template
import at.xa1.saveto.model.template.TemplatePlaceholderContext
import at.xa1.saveto.model.template.Templates
import at.xa1.saveto.model.template.fill

@Composable
fun SelectTemplate(modifier: Modifier = Modifier, args: SelectTemplateArgs) {
    BackHandler(onBack = args.onAbort)
    Column(modifier = modifier) {
        Surface(color = MaterialTheme.colors.primarySurface) {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.selectTemplateTitle)) },
                    navigationIcon = {
                        IconButton(onClick = { args.onAbort() }) {
                            Icon(Icons.Filled.Close, stringResource(id = R.string.settingsClose))
                        }
                    },
                    elevation = 0.dp
                )
            }
        }
        Scrollable {
            Column {
                val templates = args.templates.all.collectAsState()
                templates.value.forEach { item ->
                    Column(
                        Modifier
                            .clickable { args.onTemplateSelected(item) }
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = item.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                        CodeText(text = item.fill(args.context))
                    }

                    Divider()
                }
            }
        }
    }
}

@Preview
@Composable
fun SelectTemplatePreview() {
    Surface(color = MaterialTheme.colors.background) {
        SelectTemplate(
            modifier = Modifier.fillMaxSize(),
            args = SelectTemplateArgs(
                templates = FakeSettingsStore().templates,
                context = ExampleTemplatePlaceholderContext(),
                onAbort = {},
                onTemplateSelected = {}
            )
        )
    }
}

val SelectTemplateDestination = Destination<SelectTemplateArgs> {
    SelectTemplate(args = args)
}

data class SelectTemplateArgs(
    val templates: Templates,
    val context: TemplatePlaceholderContext,
    val onAbort: () -> Unit,
    val onTemplateSelected: (template: Template) -> Unit
)
