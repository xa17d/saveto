package at.xa1.saveto.feature.save

import android.net.Uri
import at.xa1.saveto.MainResult
import at.xa1.saveto.R
import at.xa1.saveto.common.android.Resources
import at.xa1.saveto.common.navigation.Coordinator
import at.xa1.saveto.model.PreviewMode
import at.xa1.saveto.model.ProposeFilenameUseCase
import at.xa1.saveto.model.SettingsStore
import at.xa1.saveto.model.Source
import at.xa1.saveto.model.SourceData
import at.xa1.saveto.model.humanReadableByteCount
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
    private val streamCopy: StreamCopy,
    private val settingsStore: SettingsStore,
    private val proposeFilenameUseCase: ProposeFilenameUseCase,
    private val resources: Resources
) : Coordinator<SaveArgs>() {
    override fun onStart() {
        super.onStart()
        scope.launch {
            when (settingsStore.previewMode) {
                PreviewMode.INTENT_DETAILS -> preview()
                PreviewMode.NONE -> save()
            }
        }
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
            val filename = proposeFilenameUseCase.getFilenameFor(args.source)
            val result = saveDialog.show(args.source.type, filename)

            if (result == null) {
                abort()
            } else {
                statusText.value = resources.string(R.string.saving)
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
                    statusText.value = resources.progressString(progress.bytesCopied)
                }
                .collect()

            if (streamCopy.progress.value.isFailed) {
                showError(streamCopy.progress.value.error!!)
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

    private fun showError(error: StreamCopyError) {
        navigator.goTo(ErrorDestination) {
            ErrorArgs(
                text = when (error.type) {
                    StreamCopyErrorType.OPEN_INPUT_STREAM_ERROR ->
                        resources.string(R.string.saveErrorOpenInputStreamError)
                    StreamCopyErrorType.OPEN_OUTPUT_STREAM_ERROR ->
                        resources.string(R.string.saveErrorOpenOutputStreamError)
                    StreamCopyErrorType.COPY_ERROR ->
                        resources.string(R.string.saveErrorCopyError)
                },
                onClose = { args.onClose(MainResult.ABORT) }
            )
        }
    }
}

internal fun Resources.progressString(bytesCopied: Long): String =
    string(
        R.string.progressBytesWritten,
        humanReadableByteCount(bytesCopied)
    )

data class SaveArgs(
    val source: Source,
    val onClose: (result: MainResult) -> Unit
)
