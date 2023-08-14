package core.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.get

open class ViewModel: KoinComponent {
    protected open val viewModelScope = MainScope()

    open fun onDispose() {
        viewModelScope.cancel()
    }
}

@Composable
inline fun <reified T : ViewModel> rememberViewModel(): T {
    val viewModel: T = get(T::class.java)

    DisposableEffect(viewModel) {
        onDispose { viewModel.onDispose() }
    }

    return viewModel
}
