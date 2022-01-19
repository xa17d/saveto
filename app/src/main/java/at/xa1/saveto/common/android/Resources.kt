package at.xa1.saveto.common.android

import android.content.Context
import androidx.annotation.StringRes

interface Resources {
    fun string(@StringRes id: Int): String
    fun string(@StringRes id: Int, vararg args: Any): String
}

class AndroidResources(
    private val context: Context
) : Resources {
    override fun string(id: Int): String =
        context.getString(id)

    override fun string(id: Int, vararg args: Any) =
        context.getString(id, *args)
}
