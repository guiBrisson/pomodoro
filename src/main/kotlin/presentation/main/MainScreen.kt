package presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import core.viewmodel.rememberViewModel
import domain.model.Task
import ui.components.BaseContainer
import ui.components.PomodoroComponent

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    selectedTask: Task?,
) {
    val viewModel: MainViewModel = rememberViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setSelectedTask(selectedTask)
        viewModel.startTimer()
    }

    MainScreen(
        modifier = modifier,
        uiState = uiState,
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
    selectedTask: Task?,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit,
) {
    BaseContainer(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PomodoroComponent(
            modifier = Modifier,
            timerValue = uiState.timer ?: 0,
            isTimeRunning = uiState.isTimerRunning,
            currentPomodoroEvent = uiState.currentPomodoroEvent,
            onResume = onResume,
            onPause = onPause,
            onNext = onNext
        )
    }
}
