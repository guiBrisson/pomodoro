package presentation.main

import domain.model.Task
import utils.PomodoroEvent

data class MainUiState(
    val selectedTask: Task? = null,
    val loading: Boolean = false,

    val timer: Int? = null,
    val isTimerRunning: Boolean = false,
    val currentPomodoroEvent: PomodoroEvent = PomodoroEvent.FOCUS,
)

sealed interface MainEvent {
    object ResumeTimer : MainEvent
    object PauseTimer : MainEvent
    object NextEvent : MainEvent
}
