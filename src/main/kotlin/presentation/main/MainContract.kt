package presentation.main

import domain.model.Task

data class MainUiState(
    val selectedTask: Task? = null,
    val timer: Int? = null,
    val isTimerRunning: Boolean = false,
    val loading: Boolean = false,
)

sealed interface MainEvent {
    data class SetupTimer(val durationOnSeconds: Int) : MainEvent
    object ResumeTimer : MainEvent
    object PauseTimer : MainEvent
    object StopTimer : MainEvent
}
