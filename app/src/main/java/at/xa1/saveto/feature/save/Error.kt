package at.xa1.saveto.feature.save

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.xa1.saveto.R
import at.xa1.saveto.android.compose.Scrollable
import at.xa1.saveto.common.navigation.Destination
import at.xa1.saveto.ui.OptionButton
import at.xa1.saveto.ui.theme.Orange500
import at.xa1.saveto.ui.theme.SaveToTheme

@Composable
fun Error(modifier: Modifier = Modifier, args: ErrorArgs) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Scrollable(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .then(modifier)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(96.dp)
                            .aspectRatio(1f),
                        painter = painterResource(id = R.drawable.ic_error_24),
                        contentDescription = stringResource(id = R.string.saveErrorIconContentDescription),
                        tint = Orange500 // TODO from theme
                    )
                    Text(
                        text = stringResource(id = R.string.errorSomethingWentWrong),
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                SelectionContainer {
                    Text(
                        text = args.text
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            OptionButton(text = stringResource(R.string.errorClose)) {
                args.onClose()
            }
        }
    }
}

@Preview
@Composable
fun ErrorPreview() {
    val args = ErrorArgs(
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam suscipit tortor " +
            "vel vulputate consectetur. Praesent dictum arcu nibh, quis gravida dui volutpat " +
            "id. Mauris sed erat et velit fermentum bibendum vel non turpis. Pellentesque a " +
            "congue elit. Vivamus at orci arcu. Orci varius natoque penatibus et magnis dis " +
            "parturient montes, nascetur ridiculus mus. Phasellus a nisl dignissim tellus " +
            "aliquam laoreet.\n\n".repeat(5),
        onClose = {}
    )

    SaveToTheme {
        Error(modifier = Modifier.fillMaxWidth(), args)
    }
}

val ErrorDestination = Destination<ErrorArgs> {
    Error(modifier = Modifier.fillMaxSize(), args)
}

data class ErrorArgs(
    val text: String,
    val onClose: () -> Unit
)
