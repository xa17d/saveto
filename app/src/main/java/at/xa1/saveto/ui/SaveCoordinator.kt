package at.xa1.saveto.ui

import android.net.Uri
import at.xa1.saveto.MainResult
import at.xa1.saveto.android.SaveDialog
import at.xa1.saveto.android.StreamCopy
import at.xa1.saveto.model.Source
import at.xa1.saveto.model.SourceData
import at.xa1.saveto.model.humanReadableByteCount
import at.xa1.saveto.model.proposedFilename
import at.xa1.saveto.navigation.Coordinator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class SaveCoordinator(
    private val scope: CoroutineScope,
    private val saveDialog: SaveDialog,
    private val streamCopy: StreamCopy
) : Coordinator<SaveArgs>() {
    override fun onStart() {
        super.onStart()
        navigator.goTo(SplashDestination)
        scope.launch { preview() }
    }

    private fun preview() {
        navigator.goTo(SourcePreviewDestination) {
            SourcePreviewArgs(
                source = args.source,
                onSave = ::save
            )
        }
    }

    private fun save() {
        val statusText = MutableStateFlow<String?>(null)
        navigator.goTo(LoadingDestination) {
            LoadingArgs(
                text = statusText
            )
        }
        scope.launch {
            val filename = args.source.proposedFilename()
            val result = saveDialog.show(args.source.type, filename)

            if (result == null) {
                abort()
            } else {
                statusText.value = "Saving..." // TODO resource
                copy(result, statusText)
            }
        }
    }

    private fun abort() {
        args.onClose(MainResult.ABORT)
    }

    private fun copy(destinationUri: Uri, statusText: MutableStateFlow<String?>) {
        scope.launch {
            launch(Dispatchers.IO) {
                when (val sourceData = args.source.data) {
                    is SourceData.Uri -> streamCopy.copy(sourceData.value, destinationUri)
                    is SourceData.Text -> streamCopy.copy(sourceData.value, destinationUri)
                    SourceData.Unknown -> error("unknown") // TODO
                }
            }

            streamCopy.progress
                .takeWhile { progress -> !progress.isFinished }
                .onEach { progress ->
                    statusText.value =
                        "${humanReadableByteCount(progress.bytesCopied)} written..." // TODO resources
                }
                .collect()

            if (streamCopy.progress.value.isFailed) {
                TODO()
            } else {
                success()
            }

        }
    }

    private fun success() {
        navigator.goTo(SuccessDestination)
        scope.launch {
            delay(500)
            args.onClose(MainResult.OK)
        }
    }
}

data class SaveArgs(
    val source: Source,
    val onClose: (result: MainResult) -> Unit
)