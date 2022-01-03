package at.xa1.saveto

import android.app.Application
import at.xa1.saveto.di.Injector

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Injector.init()
    }
}
