package at.xa1.saveto.feature.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.xa1.saveto.R
import at.xa1.saveto.android.compose.Scrollable
import at.xa1.saveto.common.navigation.Destination
import at.xa1.saveto.ui.OptionButton
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Intro(modifier: Modifier = Modifier, args: IntroArgs) {
    Column(
        Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        val pagerState = rememberPagerState()

        val scope = rememberCoroutineScope()
        fun nextPage() {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }

        val pageCount = 4
        HorizontalPager(
            count = pageCount,
            state = pagerState,
            contentPadding = PaddingValues(start = 32.dp, end = 32.dp, top = 32.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            Card(
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp)
                    .let {
                        if (page < pageCount - 1) {
                            it.clickable { nextPage() }
                        } else {
                            it
                        }
                    }
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    when (page) {
                        0 -> CardFindShared(onShareClicked = ::nextPage)
                        1 -> CardSelectShareTo()
                        2 -> CardChooseDestination()
                        3 -> CardFinish(args = args)
                        else -> error("no such page")
                    }
                }
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(24.dp)
        )
    }
}

@Composable
fun CardFindShared(onShareClicked: () -> Unit) {
    Scrollable {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.introWelcome),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(24.dp))

            Text(
                text = stringResource(id = R.string.introExplanation)
            )

            Spacer(modifier = Modifier.size(24.dp))

            Button(onClick = { onShareClicked() }, modifier = Modifier.padding(end = 8.dp)) {
                Icon(Icons.Filled.Share, stringResource(id = R.string.introShareButtonContentDescription))
                Text(text = stringResource(id = R.string.introShareButton))
            }

            Spacer(modifier = Modifier.size(24.dp))

            Text(
                text = stringResource(id = R.string.introSwipeToContinue),
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CardSelectShareTo() {
    Scrollable {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.introSelectSaveTo),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(16.dp))

            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.share_screenshot),
                contentDescription = stringResource(id = R.string.introShareBottomSheetImageContentDescription)
            )
        }
    }
}

@Composable
fun CardChooseDestination() {
    Scrollable {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.introChooseDestination),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(16.dp))

            Image(
                modifier = Modifier
                    .border(1.dp, Color.Gray)
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.savedialog_screenshot),
                contentDescription = stringResource(id = R.string.introSaveDialogImageContentDescription)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = stringResource(id = R.string.introAndDone),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CardFinish(args: IntroArgs) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        OptionButton(
            text = stringResource(id = R.string.introExitAndTry),
            onClick = { args.onClose() }
        )

        OptionButton(
            text = stringResource(id = R.string.introOpenSettings),
            onClick = { args.onSettings() }
        )
    }
}

val IntroDestination = Destination<IntroArgs> {
    Intro(modifier = Modifier.fillMaxSize(), args)
}

data class IntroArgs(
    val onSettings: () -> Unit,
    val onClose: () -> Unit
)
