package at.xa1.saveto.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
