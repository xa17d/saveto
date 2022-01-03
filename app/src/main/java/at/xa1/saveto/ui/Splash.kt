package at.xa1.saveto.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import at.xa1.saveto.R
import at.xa1.saveto.navigation.Destination
import at.xa1.saveto.ui.theme.Orange500

@Preview
@Composable
fun Splash(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .background(Orange500)
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(0.75f)
                .aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "SaveTo logo" //  TODO resource
        )
    }
}

val SplashDestination = Destination<Unit> {
    Splash(modifier = Modifier.fillMaxSize())
}
