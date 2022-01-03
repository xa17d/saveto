package at.xa1.saveto.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import at.xa1.saveto.model.Mime

class SaveDialog(
    private val intentManager: IntentManager
) {
    suspend fun show(type: Mime, filename: String): Uri? {

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = type.value
        intent.putExtra(Intent.EXTRA_TITLE, filename)

        return intentManager.startForResult(intent) { resultCode: Int, data: Intent? ->
            if (resultCode == Activity.RESULT_OK) {
                data!!.data!!
            } else {
                null
            }
        }
    }
}
