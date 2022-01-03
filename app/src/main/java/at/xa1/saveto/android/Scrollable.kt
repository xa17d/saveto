package at.xa1.saveto.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Scrollable(block: @Composable () -> Unit) {
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.verticalScroll(scrollState)) {
        block()
    }
}
