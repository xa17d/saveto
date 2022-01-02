package at.xa1.saveto.model

import android.content.Intent
import android.net.Uri
import android.os.Parcelable

fun getSourceInformation(intent: Intent): Uri {
    return intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri
        ?: intent.data
        ?: error("cannot get data")
}
