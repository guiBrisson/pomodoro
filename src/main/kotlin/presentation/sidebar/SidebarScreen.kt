package presentation.sidebar

import androidx.compose.desktop.ui.tooling.preview.Preview
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
import domain.model.Task
import ui.components.AddTaskComponent
import ui.components.BaseContainer
import ui.components.SearchComponent
import ui.components.TasksComponent
import ui.theme.PomodoroTheme


@Composable
fun SidebarScreen(
    modifier: Modifier = Modifier,
    onSendArgs: (String) -> Unit,
) {
    val viewModel: SidebarViewModel = rememberViewModel()
    val uiState by viewModel.uiState.collectAsState()

    SidebarScreen(
        modifier = modifier,
        uiState = uiState,
        onSendArgs = onSendArgs,
        onAddTask = { viewModel.handleEvents(SidebarEvent.CreateNewTask(it)) }
    )
}

@Composable
internal fun SidebarScreen(
    modifier: Modifier = Modifier,
    uiState: SidebarUiState,
    onSendArgs: (String) -> Unit,
    onAddTask: (Task) -> Unit,
) {
    BaseContainer(modifier = modifier.width(340.dp).fillMaxHeight()) {
        SearchComponent(onSearch = { onSendArgs(it) })
        TasksComponent(modifier = Modifier.weight(1f), tasks = uiState.tasks, isLoading = uiState.loadingTasks)
        AddTaskComponent(onAddTask = onAddTask)
    }
}

@Preview
@Composable
private fun PreviewSidebarScreen() {
    PomodoroTheme {
        Surface(color = MaterialTheme.colors.background) {
            val state = SidebarUiState()
            SidebarScreen(uiState = state, onSendArgs = { }, onAddTask = { })
        }
    }
}
