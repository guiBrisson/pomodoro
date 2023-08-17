package presentation.main

import domain.model.Task
import utils.PomodoroEvent

data class MainUiState(
    val selectedTask: Task? = null,
    val currentPomodoroEvent: PomodoroEvent = PomodoroEvent.FOCUS,
    val timer: Int? = null,
    val isTimerRunning: Boolean = false,
    val loading: Boolean = false,
)

sealed interface MainEvent {
    object ResumeTimer : MainEvent
    object PauseTimer : MainEvent
    object NextEvent : MainEvent
}
