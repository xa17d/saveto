package at.xa1.saveto.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import at.xa1.saveto.R
import at.xa1.saveto.model.TemplatePlaceholderContext
import java.time.ZonedDateTime

@Composable
@ReadOnlyComposable
fun ExampleTemplatePlaceholderContext(): TemplatePlaceholderContext =
    TemplatePlaceholderContext(
        time = ZonedDateTime.now(),
        originalFilename = stringResource(id = R.string.settingsTemplateExampleOriginalFilename)
    )
