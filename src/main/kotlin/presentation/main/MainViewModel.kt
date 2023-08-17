package presentation.main

import core.viewmodel.ViewModel
import domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import utils.PomodoroManager

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val pomodoroManager = PomodoroManager(sessions = 8).apply {
        onEvent = { event ->
            _uiState.update { it.copy(currentPomodoroEvent = event) }
        }
        onTick = { seconds ->
            _uiState.update { it.copy(timer = seconds, isTimerRunning = true) }
        }
        onPomodoroFinish = { println("pomodoro has finished") }
    }

    fun handleEvents(event: MainEvent) {
        when (event) {
            MainEvent.ResumeTimer -> resumeTimer()
            MainEvent.PauseTimer -> pauseTimer()
            MainEvent.NextEvent -> pomodoroManager.nextSection()
        }
    }

    fun startTimer() {
        pomodoroManager.startTimer()
        _uiState.update { it.copy(isTimerRunning = true) }
    }

    private fun pauseTimer() {
        pomodoroManager.pause()
        _uiState.update { it.copy(isTimerRunning = false) }
    }

    private fun resumeTimer() {
        pomodoroManager.resume()
        _uiState.update { it.copy(isTimerRunning = true) }
    }

    fun setSelectedTask(task: Task?) {
        _uiState.update { it.copy(selectedTask = task) }
    }
}
