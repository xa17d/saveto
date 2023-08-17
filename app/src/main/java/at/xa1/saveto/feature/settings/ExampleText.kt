package at.xa1.saveto.feature.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import at.xa1.saveto.R

@Composable
fun ExampleText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        color = Color.Gray,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(stringResource(id = R.string.exampleText))
            }
            append(" ")
            append(text)
        }
    )
}
