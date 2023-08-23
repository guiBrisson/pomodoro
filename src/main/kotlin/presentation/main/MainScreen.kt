package presentation.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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


@OptIn(ExperimentalAnimationApi::class)
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
    AnimatedContent(isCollapsed) { collapsed ->
        if (collapsed) {
            BaseContainer(
                modifier = modifier.fillMaxWidth().height(100.dp),
            ) {
                Text("collapsed main")
            }
        } else {
            BaseContainer(
                modifier = modifier.fillMaxSize(),
            ) {
                PomodoroComponent(
                    modifier = Modifier.fillMaxSize(),
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

        }
    }
}
