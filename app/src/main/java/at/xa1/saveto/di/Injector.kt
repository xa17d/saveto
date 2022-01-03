package at.xa1.saveto.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import at.xa1.saveto.MainActivity
import at.xa1.saveto.MainArgs
import at.xa1.saveto.MainCoordinator
import at.xa1.saveto.MainResult
import at.xa1.saveto.android.HostHolder
import at.xa1.saveto.android.IntentManager
import at.xa1.saveto.android.SaveDialog
import at.xa1.saveto.android.StreamCopy
import at.xa1.saveto.android.getRetainedInstance
import at.xa1.saveto.model.SettingsStore
import at.xa1.saveto.navigation.ComposeNavigator
import at.xa1.saveto.navigation.CoordinatorDestination
import at.xa1.saveto.ui.LaunchedCoordinator
import at.xa1.saveto.ui.SaveCoordinator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class Injector(private val applicationContext: Application) {

    private val settingsStore = SettingsStore(
        applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
    )

    fun inject(mainActivity: MainActivity) {

        class MainActivityInstances(intent: Intent, applicationContext: Application) {
            val hostHolder = HostHolder()
            val intentManager = IntentManager(hostHolder)
            val navigator = ComposeNavigator().apply {
                val saveCoordinatorDestination = CoordinatorDestination {
                    SaveCoordinator(
                        CoroutineScope(Dispatchers.Main),
                        SaveDialog(intentManager),
                        StreamCopy(applicationContext.contentResolver),
                        settingsStore
                    )
                }

                val launchedCoordinatorDestination = CoordinatorDestination {
                    LaunchedCoordinator(
                        settingsStore
                    )
                }

                val mainCoordinatorDestination = CoordinatorDestination {
                    MainCoordinator(
                        saveCoordinatorDestination,
                        launchedCoordinatorDestination
                    )
                }

                goTo(mainCoordinatorDestination) {
                    MainArgs(
                        intent = intent,
                        onClose = { result ->
                            hostHolder.runOrEnqueue {
                                activity.setResult(
                                    when (result) {
                                        MainResult.OK -> Activity.RESULT_OK
                                        MainResult.ABORT -> Activity.RESULT_CANCELED
                                    }
                                )
                                activity.finish()
                            }
                        }
                    )
                }
            }
        }

        val instances = getRetainedInstance(mainActivity) {
            MainActivityInstances(mainActivity.intent, mainActivity.application)
        }

        mainActivity.apply {
            hostHolder = instances.hostHolder
            intentManager = instances.intentManager
            navigator = instances.navigator
        }
    }

    companion object {
        lateinit var INSTANCE: Injector
            private set

        fun init(applicationContext: Application) {
            INSTANCE = Injector(applicationContext)
        }
    }
}

fun getInjector(): Injector {
    return Injector.INSTANCE
}

/**
 * Just a marker
 */
@Retention(AnnotationRetention.SOURCE)
annotation class Inject
