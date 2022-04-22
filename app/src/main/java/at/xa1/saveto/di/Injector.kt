package at.xa1.saveto.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import at.xa1.saveto.LaunchedCoordinator
import at.xa1.saveto.MainActivity
import at.xa1.saveto.MainArgs
import at.xa1.saveto.MainCoordinator
import at.xa1.saveto.MainResult
import at.xa1.saveto.R
import at.xa1.saveto.common.android.AndroidResources
import at.xa1.saveto.common.android.IntentManager
import at.xa1.saveto.common.android.getRetainedInstance
import at.xa1.saveto.common.navigation.ComposeNavigator
import at.xa1.saveto.common.navigation.CoordinatorDestination
import at.xa1.saveto.common.navigation.HostHolder
import at.xa1.saveto.feature.save.AndroidContentResolver
import at.xa1.saveto.feature.save.SaveCoordinator
import at.xa1.saveto.feature.save.SaveDialog
import at.xa1.saveto.feature.save.StreamCopy
import at.xa1.saveto.model.ProposeFilenameUseCase
import at.xa1.saveto.model.SharedPreferencesSettingsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class Injector(private val applicationContext: Application) {

    private val settingsStore = SharedPreferencesSettingsStore(
        applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
    )

    private val resources = AndroidResources(applicationContext)

    fun inject(mainActivity: MainActivity) {

        class MainActivityInstances(intent: Intent) {
            val hostHolder = HostHolder()
            val intentManager = IntentManager(hostHolder)
            val androidContentResolver = AndroidContentResolver(applicationContext.contentResolver)
            val navigator = ComposeNavigator().apply {
                val saveCoordinatorDestination = CoordinatorDestination {
                    SaveCoordinator(
                        CoroutineScope(Dispatchers.Main),
                        SaveDialog(intentManager),
                        StreamCopy(androidContentResolver),
                        settingsStore,
                        ProposeFilenameUseCase(androidContentResolver, resources.string(R.string.defaultFilename)),
                        resources
                    )
                }

                val launchedCoordinatorDestination = CoordinatorDestination {
                    LaunchedCoordinator(
                        settingsStore, hostHolder
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
            MainActivityInstances(mainActivity.intent)
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
 * Just a marker.
 * Injection is performed manually using [Injector].
 * Could be replaced by Dagger in the future.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class Inject
