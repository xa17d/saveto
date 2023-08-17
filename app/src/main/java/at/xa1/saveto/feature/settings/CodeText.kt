package at.xa1.saveto.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun CodeText(text: String) {
    Text(
        modifier = Modifier
            .background(codeTextBackground, shape = RoundedCornerShape(8.dp))
            .padding(4.dp),
        text = text,
        fontFamily = FontFamily.Monospace
    )
}

private val codeTextBackground: Color = Color.LightGray.copy(alpha = 0.5f)
