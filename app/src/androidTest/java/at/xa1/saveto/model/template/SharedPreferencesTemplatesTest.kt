package at.xa1.saveto.model.template

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import at.xa1.saveto.R
import at.xa1.saveto.common.android.FakeResources
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SharedPreferencesTemplatesTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var resources: FakeResources

    @Before
    fun setup() {
        val context = getApplicationContext<Application>()
        sharedPreferences = context.getSharedPreferences(
            "SharedPreferencesTemplatesTest",
            MODE_PRIVATE
        )
        sharedPreferences.edit().clear().apply()

        resources = FakeResources().apply {
            given(R.string.settingsTemplateDefaultName, "Default")
        }
    }

    @Test
    fun changesArePersistedAndRestored() = runTest {
        val instance = SharedPreferencesTemplates(
            sharedPreferences = sharedPreferences,
            resources = resources,
            saveScope = backgroundScope
        )

        instance.templates.add(
            Template(
                name = "Test Name",
                suggestedFilename = "Test Suggested Filename",
                addExtensionIfMissing = false
            )
        )

        yield() // allow backgroundScope to run and persist the added template

        val storedValues = instance.templates.all.value

        val restoredInstance = SharedPreferencesTemplates(
            sharedPreferences = sharedPreferences,
            resources = resources,
            saveScope = backgroundScope
        )

        assertEquals(
            storedValues,
            restoredInstance.templates.all.value
        )
    }
}
