package core.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

open class ViewModel {
    protected open val viewModelScope = MainScope()

    open fun onDispose() {
        viewModelScope.cancel()
        println("viewModel scope is canceled")
    }
}

@Composable
inline fun <reified T : ViewModel> rememberViewModel(
    noinline viewModelProvider: @DisallowComposableCalls () -> T
): T {
    val viewModel = remember(viewModelProvider) { viewModelProvider() }

    DisposableEffect(viewModel) {
        onDispose { viewModel.onDispose() }
    }

    return viewModel
}
