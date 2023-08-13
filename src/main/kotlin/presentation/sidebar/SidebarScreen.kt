package presentation.sidebar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.viewmodel.rememberViewModel
import ui.components.AddTaskComponent
import ui.components.BaseContainer
import ui.components.SearchComponent
import ui.theme.PomodoroTheme

@Composable
fun SidebarScreen(
    modifier: Modifier = Modifier,
    onSendArgs: (String) -> Unit,
) {
    val viewModel = rememberViewModel { SidebarViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    SidebarScreen(
        modifier = modifier,
        uiState = uiState,
        onSendArgs = onSendArgs
    )
}

@Composable
internal fun SidebarScreen(
    modifier: Modifier = Modifier,
    uiState: SidebarUiState,
    onSendArgs: (String) -> Unit,
) {
    BaseContainer(modifier = modifier.width(340.dp).fillMaxHeight()) {
        SearchComponent(onSearch = { onSendArgs(it) })
        Spacer(modifier = Modifier.weight(1f))
        AddTaskComponent()
    }
}

@Preview
@Composable
private fun PreviewSidebarScreen() {
    PomodoroTheme {
        Surface(color = MaterialTheme.colors.background) {
            val state = SidebarUiState()
            SidebarScreen(uiState = state, onSendArgs = { })
        }
    }
}
