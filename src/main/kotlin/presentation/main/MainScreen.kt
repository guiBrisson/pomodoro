package presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import core.viewmodel.rememberViewModel
import domain.model.Task
import ui.components.PomodoroComponent

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    selectedTask: Task?,
    isCollapsed: Boolean,
    onSetting: () -> Unit,
) {
    val viewModel: MainViewModel = rememberViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(selectedTask) {
        if (uiState.selectedTask != selectedTask) {
            viewModel.setSelectedTask(selectedTask)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startTimer()
    }

    MainScreen(
        modifier = modifier,
        uiState = uiState,
        isCollapsed = isCollapsed,
        onSetting = onSetting,
        selectedTask = uiState.selectedTask,
        onResume = { viewModel.handleEvents(MainEvent.ResumeTimer) },
        onPause = { viewModel.handleEvents(MainEvent.PauseTimer) },
        onNext = { viewModel.handleEvents(MainEvent.NextEvent) }
    )
}

@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    isCollapsed: Boolean,
    onSetting: () -> Unit,
    selectedTask: Task?,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit,
) {
    PomodoroComponent(
        modifier = modifier,
        isCollapsed = isCollapsed,
        selectedTask = selectedTask,
        timerValue = uiState.timer ?: 0,
        isTimeRunning = uiState.isTimerRunning,
        currentPomodoroEvent = uiState.currentPomodoroEvent,
        onSetting = onSetting,
        onResume = onResume,
        onPause = onPause,
        onNext = onNext,
    )

}
