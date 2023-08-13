package presentation.sidebar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.viewmodel.rememberViewModel
import ui.components.BaseContainer
import ui.theme.PomodoroTheme

@Composable
fun SidebarScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = rememberViewModel { SidebarViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    SidebarScreen(
        modifier = modifier,
        uiState = uiState,
    )
}

@Composable
internal fun SidebarScreen(
    modifier: Modifier = Modifier,
    uiState: SidebarUiState,
) {
    BaseContainer(modifier = modifier.width(340.dp).fillMaxHeight()) {

    }
}

@Preview
@Composable
private fun PreviewSidebarScreen() {
    PomodoroTheme {
        Surface(color = MaterialTheme.colors.background) {
            val state = SidebarUiState()
            SidebarScreen(uiState = state)
        }
    }
}
