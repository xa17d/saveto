package at.xa1.saveto.feature.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import at.xa1.saveto.common.android.IntentManager

class OpenDocumentTreeDialog(
    private val intentManager: IntentManager
) {
    suspend fun show(): Uri? {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

        return intentManager.startForResult(intent) { resultCode: Int, data: Intent? ->
            if (resultCode == Activity.RESULT_OK) {
                data!!.data!!
            } else {
                null
            }
        }
    }
}
