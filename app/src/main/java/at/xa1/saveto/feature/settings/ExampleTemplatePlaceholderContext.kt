package at.xa1.saveto.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import at.xa1.saveto.R
import at.xa1.saveto.model.Mime
import at.xa1.saveto.model.template.TemplatePlaceholderContext
import java.time.ZonedDateTime

@Composable
@ReadOnlyComposable
fun ExampleTemplatePlaceholderContext(): TemplatePlaceholderContext =
    TemplatePlaceholderContext(
        time = ZonedDateTime.now(),
        type = Mime.from("text/plain"),
        originalFilename = stringResource(id = R.string.settingsTemplateExampleOriginalFilename)
    )
