package at.xa1.saveto.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.xa1.saveto.android.Scrollable
import at.xa1.saveto.model.getSourceInformation
import at.xa1.saveto.navigation.Destination

@Composable
fun SourcePreview(source: Source, onSave: () -> Unit) {
    Scrollable {
        Column {
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = onSave
            ) {
                Text(text = "Save")
            }
            IntentBox(intent = source.intent)
        }
    }
}

@Composable
fun IntentBox(intent: Intent) {
    Card(elevation = 4.dp, modifier = Modifier.padding(8.dp)) {
        Column {
            IntentProperty(name = "action", value = intent.action)
            IntentProperty(name = "type", value = intent.type)
            IntentProperty(name = "data", value = intent.dataString)
            IntentProperty(name = "categories", value = intent.categories?.joinToString())

            val extras = intent.extras
            if (extras != null) {
                extras.keySet().forEach { key ->
                    IntentProperty(name = "extra: $key", value = extras.get(key))
                }
            } else {
                IntentProperty(name = "extras", value = null)
            }
        }
    }
}

@Composable
private fun IntentProperty(name: String, value: Any?) {
    TextField(
        value = value?.toString() ?: "null",
        readOnly = true,
        enabled = (value != null),
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        label = { Text(name) }
    )
}

val SourcePreviewDestination = Destination<SourcePreviewArgs> {
    SourcePreview(source = args.source, onSave = args.onSave)
}

data class SourcePreviewArgs(
    val source: Source,
    val onSave: () -> Unit
)

data class Source(
    val intent: Intent,
    val type: String
) {
    val sourceUri: Uri
        get() = getSourceInformation(intent) // TODO fix
}
