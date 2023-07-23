package at.xa1.saveto.feature.save

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import at.xa1.saveto.common.android.IntentManager
import at.xa1.saveto.model.Mime

class SaveDialog(
    private val intentManager: IntentManager
) {
    suspend fun show(type: Mime, filename: String, initialLocationUri: Uri? = null): Uri? {
        return intentManager.startForResult(
            intentProvider = { host ->
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = type.value
                intent.putExtra(Intent.EXTRA_TITLE, filename)

                if (initialLocationUri != null) {
                    // For some reason, the result of a `Intent.ACTION_OPEN_DOCUMENT_TREE` dialog needs to be converted
                    // by `DocumentFile`.
                    // See https://stackoverflow.com/a/54099021
                    val file = DocumentFile.fromTreeUri(host.activity, initialLocationUri)
                    // TODO log if file is null
                    val uri = file?.uri ?: initialLocationUri
                    intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
                }

                intent
            },
            resultTransform = { resultCode: Int, data: Intent? ->
                if (resultCode == Activity.RESULT_OK) {
                    data!!.data!!
                } else {
                    null
                }
            }
        )
    }
}
