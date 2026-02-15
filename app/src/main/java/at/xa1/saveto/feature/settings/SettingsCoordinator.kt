@file:Suppress("DEPRECATION") // OssLicensesMenuActivity has no replacement

package at.xa1.saveto.feature.settings

import android.content.Intent
import android.net.Uri
import at.xa1.saveto.R
import at.xa1.saveto.common.android.Resources
import at.xa1.saveto.common.navigation.Coordinator
import at.xa1.saveto.common.navigation.HostHolder
import at.xa1.saveto.model.SettingsStore
import at.xa1.saveto.model.template.Template
import at.xa1.saveto.model.template.TemplateId
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class SettingsCoordinator(
    private val settingsStore: SettingsStore,
    private val resources: Resources,
    private val hostHolder: HostHolder
) : Coordinator<SettingsCoordinatorArgs>() {
    override fun onStart() {
        super.onStart()
        settingsMain()
    }

    private fun settingsMain() {
        navigator.goTo(SettingsDestination) {
            SettingsArgs(
                settingsStore = settingsStore,
                onOssLicenses = ::openOssLicenses,
                onIntro = args.onIntro,
                onClose = args.onClose,
                onAddTemplate = ::settings,
                onRemoveTemplate = ::removeTemplate,
                onEditTemplate = ::editTemplate,
                onContact = ::openContact
            )
        }
    }

    private fun settings() {
        navigator.goTo(TemplateSettingsDestination) {
            TemplateSettingsArgs(
                template = Template(name = resources.string(R.string.settingsTemplateDefaultNewName)),
                onBack = ::settingsMain,
                onSave = { template ->
                    settingsStore.templates.add(template)
                    settingsMain()
                }
            )
        }
    }

    private fun removeTemplate(id: TemplateId) {
        settingsStore.templates.remove(id)
    }

    private fun editTemplate(template: Template) {
        navigator.goTo(TemplateSettingsDestination) {
            TemplateSettingsArgs(
                template = template,
                onBack = ::settingsMain,
                onSave = { updatedTemplate ->
                    settingsStore.templates.update(updatedTemplate)
                    settingsMain()
                }
            )
        }
    }

    private fun openContact() {
        hostHolder.runOrEnqueue {
            activity.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://xa1.at/saveto/contact")
                }
            )
        }
    }

    private fun openOssLicenses() {
        hostHolder.runOrEnqueue {
            activity.startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
        }
    }
}

data class SettingsCoordinatorArgs(
    val onIntro: () -> Unit,
    val onClose: () -> Unit
)
