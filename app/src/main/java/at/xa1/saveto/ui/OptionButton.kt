package at.xa1.saveto.ui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OptionButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .defaultMinSize(minHeight = 48.dp)
    ) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}
