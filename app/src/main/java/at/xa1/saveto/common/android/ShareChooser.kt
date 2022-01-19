package at.xa1.saveto.common.android

import android.content.Context
import android.content.Intent
import android.net.Uri

class ShareChooser(private val context: Context) {
    fun open() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            // putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            putExtra(Intent.EXTRA_SUBJECT, "my image")
            putExtra(Intent.EXTRA_STREAM, Uri.parse("https://example.com/my image.png"))
            type = "image/png" // TODO generalize
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
}
