package at.xa1.saveto.feature.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import at.xa1.saveto.R
import at.xa1.saveto.android.compose.Scrollable
import at.xa1.saveto.common.navigation.Destination
import at.xa1.saveto.model.template.Template
import at.xa1.saveto.model.template.TemplatePlaceholder
import at.xa1.saveto.model.template.TemplatePlaceholderContext
import at.xa1.saveto.model.template.replacementPattern
import at.xa1.saveto.ui.OptionButton

@Composable
fun TemplateSetting(modifier: Modifier = Modifier, args: TemplateSettingsArgs) {
    BackHandler(onBack = args.onBack)

    var templateName by remember { mutableStateOf(args.template.name) }
    var placeholderListCollapsed by remember { mutableStateOf(true) }
    val suggestedFilename = remember { mutableStateOf(args.template.suggestedFilename) }
    var addExtensionIfMissing by remember { mutableStateOf(args.template.addExtensionIfMissing) }

    val onSave: () -> Unit = {
        args.onSave(
            args.template.copy(
                name = templateName,
                suggestedFilename = suggestedFilename.value,
                addExtensionIfMissing = addExtensionIfMissing
            )
        )
    }

    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = "Edit Template") },
            navigationIcon = {
                IconButton(onClick = args.onBack) {
                    Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.settingsClose))
                }
            },
            actions = {
                IconButton(onClick = onSave) {
                    Icon(Icons.Filled.Check, stringResource(id = R.string.settingsSave))
                }
            }
        )

        Scrollable {
            Column {
                TextField(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    value = templateName,
                    onValueChange = {
                        templateName = it
                    },
                    label = { Text(text = stringResource(id = R.string.settingsTemplateName)) }
                )

                TextField(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            placeholderListCollapsed = !placeholderListCollapsed
                        }) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "")
                        }
                    },
                    value = suggestedFilename.value,
                    onValueChange = {
                        suggestedFilename.value = it
                    },
                    label = { Text(text = stringResource(id = R.string.settingsTemplateSuggestedFilename)) }
                )

                val exampleContext = ExampleTemplatePlaceholderContext()
                ExampleText(
                    modifier = Modifier.padding(8.dp),
                    text = TemplatePlaceholder.fill(suggestedFilename.value, exampleContext)
                )

                if (!placeholderListCollapsed) {
                    Column {
                        TemplatePlaceholder.entries.forEach { placeholder ->
                            PlaceholderItem(suggestedFilename, placeholder, exampleContext)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            addExtensionIfMissing = !addExtensionIfMissing
                        }
                ) {
                    Checkbox(
                        checked = addExtensionIfMissing,
                        onCheckedChange = { addExtensionIfMissing = it }
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = stringResource(id = R.string.settingsTemplateAddExtensionIfMissing)
                    )
                }

                OptionButton(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = stringResource(id = R.string.settingsSave),
                    onClick = onSave
                )
            }
        }
    }
}

@Composable
private fun PlaceholderItem(
    suggestedFilename: MutableState<String>,
    placeholder: TemplatePlaceholder,
    exampleContext: TemplatePlaceholderContext
) {
    Column(
        Modifier
            .clickable { suggestedFilename.value += placeholder.replacementPattern }
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
        ) {
            CodeText(text = placeholder.replacementPattern)
            Text(
                text = stringResource(id = placeholder.description),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 4.dp)
            )
        }

        ExampleText(
            modifier = Modifier.padding(vertical = 4.dp),
            text = placeholder.value(exampleContext)
        )
    }
}

val TemplateSettingsDestination = Destination<TemplateSettingsArgs> {
    TemplateSetting(modifier = Modifier.fillMaxSize(), args)
}

data class TemplateSettingsArgs(
    val template: Template,
    val onBack: () -> Unit,
    val onSave: (template: Template) -> Unit
)
