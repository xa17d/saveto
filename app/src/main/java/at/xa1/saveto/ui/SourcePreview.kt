package at.xa1.saveto.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SourcePreview(source: Source, onSave: () -> Unit) {
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

data class Source(
    val intent: Intent
)
