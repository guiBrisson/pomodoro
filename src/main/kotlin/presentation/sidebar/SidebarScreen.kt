package presentation.sidebar

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.viewmodel.rememberViewModel
import domain.model.Task
import ui.components.AddTaskComponent
import ui.components.BaseContainer
import ui.components.SearchComponent
import ui.components.TasksComponent


@Composable
fun SidebarScreen(
    modifier: Modifier = Modifier,
    onTaskSelected: (Task?) -> Unit,
) {
    val viewModel: SidebarViewModel = rememberViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.selectedTask) {
        onTaskSelected(uiState.selectedTask)
    }

    SidebarScreen(
        modifier = modifier,
        uiState = uiState,
        onTaskSelected = { viewModel.handleEvents(SidebarEvent.SelectTask(it)) },
        onClearCompletedTasks = { viewModel.handleEvents(SidebarEvent.ClearCompletedTasks) },
        onClearAllTasks = { viewModel.handleEvents(SidebarEvent.ClearAllTasks) },
        onAddTask = { viewModel.handleEvents(SidebarEvent.CreateNewTask(it)) },
        onTaskEdit = { viewModel.handleEvents(SidebarEvent.EditTask(it)) },
        onTaskDone = { viewModel.handleEvents(SidebarEvent.MarkTaskAsDone(it)) },
        onRestart = { viewModel.handleEvents(SidebarEvent.RestartTask(it)) },
        onTaskDelete = { viewModel.handleEvents(SidebarEvent.DeleteTask(it)) },
    )
}

@Composable
internal fun SidebarScreen(
    modifier: Modifier = Modifier,
    uiState: SidebarUiState,
    onTaskSelected: (Task?) -> Unit,
    onClearCompletedTasks: () -> Unit,
    onClearAllTasks: () -> Unit,
    onAddTask: (Task) -> Unit,
    onTaskEdit: (Task) -> Unit,
    onTaskDone: (Task) -> Unit,
    onRestart: (Task) -> Unit,
    onTaskDelete: (Task) -> Unit,
) {
    BaseContainer(modifier = modifier.width(340.dp).fillMaxHeight()) {
        var taskToEdit: Task? by remember { mutableStateOf(null) }

        SearchComponent(onSearch = { /*TODO*/ })

        TasksComponent(
            modifier = Modifier.weight(1f),
            tasks = uiState.tasks,
            selectedTask = uiState.selectedTask,
            onTaskSelected = onTaskSelected,
            isLoading = uiState.loadingTasks,
            onClearCompletedTasks = onClearCompletedTasks,
            onClearAllTasks = onClearAllTasks,
            onEdit = { taskToEdit = it },
            onDone = onTaskDone,
            onRestart = onRestart,
            onDelete = onTaskDelete,
        )

        AddTaskComponent(
            task = taskToEdit,
            onAddTask = onAddTask,
            onSaveEdit = { onTaskEdit(it); taskToEdit = null },
            onCancel = { taskToEdit = null }
        )
    }
}
