package at.xa1.saveto.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Templates(
    initial: List<Template>
) {
    private val _all = MutableStateFlow(initial)

    val all: StateFlow<List<Template>> = _all.asStateFlow()
    fun add(template: Template) {
        val existingList = _all.value

        if (existingList.any { it.id == template.id }) {
            error("Template with ID already exists: ${template.id}")
        }

        val newList = existingList + template
        _all.value = newList
    }

    fun remove(id: TemplateId) {
        val existingList = _all.value

        val newList = existingList.filter { template -> template.id != id }

        if (existingList == newList) {
            error("Template with ID doesn't contain ID: $id")
        }

        _all.value = newList
    }

    fun update(template: Template) {
        val newList = _all.value.map { existingTemplate ->
            if (existingTemplate.id == template.id) {
                template
            } else {
                existingTemplate
            }
        }

        _all.value = newList
    }
}
