package at.xa1.saveto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class Coordinator<Args> {

    private var _args: Args? = null
    private var _navigator: Navigator? = null
    protected val navigator: Navigator
        get() = _navigator ?: error("not started yet")

    protected val args: Args
        get() = _args ?: error("not started yet")

    internal fun start(navigator: Navigator, args: Args) {
        if (_navigator != null) {
            error("already started")
        }

        _navigator = navigator
        _args = args
        onStart()
    }

    protected open fun onStart() {}
}

abstract class Navigator {
    fun goTo(destination: Destination<Unit>) {
        goToIntern(destination, Unit)
    }

    inline fun <Args> goTo(destination: Destination<Args>, args: () -> Args) {
        goToIntern(destination, args())
    }

    @PublishedApi
    internal abstract fun <Args> goToIntern(destination: Destination<Args>, args: Args)
}

class ComposeNavigator : Navigator() {
    private val _screen = MutableStateFlow<ComposableScreen> { }
    val screen: StateFlow<ComposableScreen>
        get() = _screen

    override fun <Args> goToIntern(destination: Destination<Args>, args: Args) {

        when (destination) {
            is ComposeDestination<Args> -> {
                val context = LocationContext(args = args)
                _screen.value = { destination.composable(context) }
            }
            is CoordinatorDestinationImpl<Args> -> {
                val coordinator = destination.factory.invoke()
                coordinator.start(this, args)
            }
            else -> {
                error("Unsupported destination type")
            }
        }
    }

    @Composable
    fun Show() {
        val s = screen.collectAsState()
        s.value.invoke()
    }
}

typealias ComposableScreen = @Composable () -> Unit

interface Destination<Args>

class LocationContext<Args>(val args: Args)

class ComposeDestination<Args>(
    val composable: @Composable LocationContext<Args>.() -> Unit
) : Destination<Args>

fun <Args> Destination(composable: @Composable LocationContext<Args>.() -> Unit) =
    ComposeDestination(composable)

fun <Args> CoordinatorDestination(factory: () -> Coordinator<Args>) =
    CoordinatorDestinationImpl(factory)

class CoordinatorDestinationImpl<Args>(val factory: () -> Coordinator<Args>) : Destination<Args>
