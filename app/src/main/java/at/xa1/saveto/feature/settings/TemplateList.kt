package at.xa1.saveto.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import at.xa1.saveto.R
import at.xa1.saveto.model.Template
import at.xa1.saveto.model.TemplateId
import at.xa1.saveto.model.TemplatePlaceholder
import at.xa1.saveto.model.Templates

@Composable
fun TemplateList(
    templates: Templates,
    onAddTemplate: () -> Unit,
    onRemoveTemplate: (id: TemplateId) -> Unit,
    onEditTemplate: (template: Template) -> Unit
) {
    Column {
        Divider()
        val items = templates.all.collectAsState()
        val canDelete = items.value.size > 1
        val exampleContext = ExampleTemplatePlaceholderContext()

        items.value.forEach { item ->
            Row(
                Modifier
                    .clickable { onEditTemplate(item) }
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text(text = item.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                    CodeText(text = item.suggestedFilename)
                    ExampleText(text = TemplatePlaceholder.fill(item.suggestedFilename, exampleContext))
                }

                if (canDelete) {
                    IconButton(
                        onClick = { onRemoveTemplate(item.id) },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            tint = MaterialTheme.colors.primary,
                            contentDescription = stringResource(id = R.string.settingsTemplateDelete)
                        )
                    }
                }
            }

            Divider()
        }

        IconButton(
            onClick = onAddTemplate,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                tint = MaterialTheme.colors.primary,
                contentDescription = stringResource(id = R.string.settingsTemplateAdd)
            )
        }
    }
}
