package presentation.main

import core.viewmodel.ViewModel
import domain.model.Task
import domain.repository.ITaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.PomodoroEvent
import utils.PomodoroManager

class MainViewModel(
    private val taskRepository: ITaskRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var pomodoroManager = getPomodoroManager(999)

    private fun getPomodoroManager(sessions: Int): PomodoroManager {
        return PomodoroManager(sessions).apply {
            onEvent = { event ->
                println("current pomodoro event: $event")
                _uiState.update { it.copy(currentPomodoroEvent = event) }

                _uiState.value.selectedTask?.let { _ ->
                    if (event != PomodoroEvent.FOCUS) {
                        // todo: needs to complete a pomodoro

                    }
                }
            }

            onTick = { seconds ->
                println(seconds)
                _uiState.update { it.copy(timer = seconds, isTimerRunning = true) }
            }

            onPomodoroFinish = {
                finishCurrentTask()
                stopTimer()
            }
        }
    }

    fun handleEvents(event: MainEvent) {
        when (event) {
            MainEvent.ResumeTimer -> resumeTimer()
            MainEvent.PauseTimer -> pauseTimer()
            MainEvent.NextEvent -> onNext()
        }
    }

    private fun finishCurrentTask() {
        _uiState.value.selectedTask?.let { selectedTask ->
            viewModelScope.launch(Dispatchers.IO) {
                val totalAmount = selectedTask.totalAmount
                val updatedTask = selectedTask.copy(isCompleted = true, amountDone = totalAmount)
                taskRepository.updateTask(updatedTask)
            }
        }
    }

    private fun onNext() {
        _uiState.value.selectedTask?.let { selectedTask ->
            if (selectedTask.isCompleted || selectedTask.amountDone == selectedTask.totalAmount) return
        }

        pomodoroManager.nextSection()
    }

    fun startTimer() {
        stopTimer()
        val task = _uiState.value.selectedTask

        val sessions = task?.totalAmount ?: 999
        pomodoroManager = getPomodoroManager(sessions)

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

    private fun stopTimer() {
        pomodoroManager.stop()
        _uiState.update { it.copy(timer = 0, isTimerRunning = false) }
    }

    fun setSelectedTask(task: Task?) {
        _uiState.update { it.copy(selectedTask = task) }

        stopTimer()
        if (task != null && (task.isCompleted || task.amountDone == task.totalAmount)) return

        startTimer()
    }
}
