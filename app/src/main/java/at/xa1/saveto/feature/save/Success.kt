package at.xa1.saveto.feature.save

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import at.xa1.saveto.R
import at.xa1.saveto.navigation.Destination
import at.xa1.saveto.ui.theme.Orange500

@Composable
fun Success(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = loadingIndicatorModifier,
            painter = painterResource(id = R.drawable.ic_check_circle_24),
            contentDescription = "Success",
            tint = Orange500 // TODO from theme
        )
    }
}

@Preview
@Composable
fun SuccessPreview() {
    Success(modifier = Modifier.fillMaxWidth())
}

val SuccessDestination = Destination<Unit> {
    Success(modifier = Modifier.fillMaxSize())
}
