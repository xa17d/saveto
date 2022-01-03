package at.xa1.saveto.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.xa1.saveto.navigation.Destination
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Intro(modifier: Modifier = Modifier, args: IntroArgs) {

    Column(Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState()

        HorizontalPager(
            count = 4,
            state = pagerState,
            contentPadding = PaddingValues(32.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            Card(
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                when (page) {
                    0 -> CardFindShared()
                    1 -> CardSelectShareTo()
                    2 -> CardChooseDestination()
                    3 -> CardFinish(args = args)
                    else -> error("no such page")
                }
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )

    }
}

@Composable
fun CardFindShared() {
    Text(text = "find the shared symbol")
}

@Composable
fun CardSelectShareTo() {
    Text(text = "select share to")
}

@Composable
fun CardChooseDestination() {
    Text(text = "choose destination and save")
}

@Composable
fun CardFinish(args: IntroArgs) {
    Column {
        Button(onClick = { args.onClose() }) {
            Text(text = "Exit and Try yourself") // TODO resources
        }

        // TODO add "open share dialog now? to try it out.

        Button(onClick = { args.onCompleted() }) { // TODO rename arg?
            Text(text = "Settings") // TODO resources
        }
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}

val IntroDestination = Destination<IntroArgs> {
    Intro(modifier = Modifier.fillMaxSize(), args)
}

data class IntroArgs(
    val onCompleted: () -> Unit,
    val onClose: () -> Unit
)
