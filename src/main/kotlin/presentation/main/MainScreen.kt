package presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val totalSeconds = (1 * 60)

    LaunchedEffect(Unit) {
        viewModel.setSelectedTask(selectedTask)
        viewModel.handleEvents(MainEvent.SetupTimer(totalSeconds))
    }

    MainScreen(
        modifier = modifier,
        uiState = uiState,
        selectedTask = uiState.selectedTask,
        timerTotal = totalSeconds,
        onResume = { viewModel.handleEvents(MainEvent.ResumeTimer) },
        onPause = { viewModel.handleEvents(MainEvent.PauseTimer) },
        onStop = { viewModel.handleEvents(MainEvent.StopTimer) },
    )
}


@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    selectedTask: Task?,
    timerTotal: Int,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit
) {
    BaseContainer(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PomodoroComponent(
            modifier = Modifier.size(400.dp),
            timerValue = uiState.timer ?: 0,
            isTimeRunning = uiState.isTimerRunning,
            timerTotal = timerTotal,
            onResume = onResume,
            onPause = onPause,
            onStop = onStop,
            onNext = { /*TODO*/ }
        )
    }
}
