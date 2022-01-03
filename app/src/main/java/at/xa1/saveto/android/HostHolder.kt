package at.xa1.saveto.android

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

typealias HostAction = HostHolder.Host.() -> Unit

class HostHolder {

    private var host: Host? = null
    private val enqueuedActions = mutableListOf<HostAction>()
    fun runOrEnqueue(action: HostAction) {
        enqueuedActions.add(action)
        tryExecute()
    }

    private fun tryExecute() {
        val host = this.host
        if (host != null) {
            while (enqueuedActions.isNotEmpty()) {
                enqueuedActions.removeAt(0).invoke(host)
            }
        }
    }

    fun attach(activity: ComponentActivity) {
        if (this.host != null) {
            error("already attached")
        }

        this.host = Host(activity)

        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)

                detach(activity)
            }
        })
    }

    private fun detach(activity: ComponentActivity) {
        if (activity == this.host?.activity) {
            this.host = null
        } else {
            error("can only detach attached context")
        }
    }

    class Host(val activity: ComponentActivity)
}
