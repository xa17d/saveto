package at.xa1.saveto.common.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

internal fun <T> getRetainedInstance(
    viewModelStoreOwner: ViewModelStoreOwner,
    factory: () -> T
): T {
    @Suppress("UNCHECKED_CAST")
    val viewModelHolder = ViewModelProvider(
        viewModelStoreOwner,
        InstanceHolderAndroidViewModelFactory(factory)
    ).get(
        KEY_RETAINED_VIEWMODEL,
        InstanceHolderAndroidViewModel::class.java
    ) as InstanceHolderAndroidViewModel<T>

    return viewModelHolder.instance
}

private const val KEY_RETAINED_VIEWMODEL =
    "at.xa1.saveto.android:InstanceHolder"

private class InstanceHolderAndroidViewModelFactory<T>(
    private val factory: () -> T
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return InstanceHolderAndroidViewModel(factory.invoke()) as T
    }
}

private class InstanceHolderAndroidViewModel<T>(val instance: T) : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        if (instance is Clearable) {
            instance.onCleared()
        }
    }
}

interface Clearable {
    fun onCleared()
}
