package at.xa1.saveto.feature.save

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.xa1.saveto.common.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

val loadingIndicatorModifier: Modifier
    get() = Modifier
        .fillMaxWidth(0.33f)
        .aspectRatio(1f)

@Composable
fun Loading(modifier: Modifier = Modifier, text: StateFlow<String?>) {
    Column(
        modifier = Modifier
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = loadingIndicatorModifier)

        val currentText = text.collectAsState().value
        if (currentText != null) {
            Text(modifier = Modifier.padding(16.dp), text = currentText)
        }
    }
}

@Preview
@Composable
fun LoadingPreview() {
    Surface(color = MaterialTheme.colors.background) {
        Loading(modifier = Modifier.fillMaxSize(), MutableStateFlow("Hello!"))
    }
}

val LoadingDestination = Destination<LoadingArgs> {
    Loading(modifier = Modifier.fillMaxSize(), args.text)
}

data class LoadingArgs(
    val text: StateFlow<String?> = MutableStateFlow(null)
)
