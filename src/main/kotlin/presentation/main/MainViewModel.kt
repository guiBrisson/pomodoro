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
import utils.PomodoroManager

class MainViewModel (
    private val taskRepository: ITaskRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var pomodoroManager = getPomodoroManager(999)

    private fun getPomodoroManager(sessions: Int): PomodoroManager {
        return PomodoroManager(sessions).apply {
            onEvent = { event ->
                _uiState.update { it.copy(currentPomodoroEvent = event) }
            }
            onTick = { seconds ->
                _uiState.update { it.copy(timer = seconds, isTimerRunning = true) }
            }
            onPomodoroFinish = {
                pomodoroManager.stop()
                finishCurrentTask()
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

    private fun onNext() {
        updateCurrentTask()
        pomodoroManager.nextSection()
    }

    private fun finishCurrentTask() {
        _uiState.update { it.copy(timer = 0, isTimerRunning = false) }

        _uiState.value.selectedTask?.let { selectedTask ->
            viewModelScope.launch(Dispatchers.IO) {
                val amountDone = selectedTask.amountDone + 1
                val isCompleted = (selectedTask.amountDone == selectedTask.totalAmount)
                val updatedTask = selectedTask.copy(amountDone = amountDone, isCompleted = isCompleted)

                taskRepository.updateTask(updatedTask)
            }
        }
    }

    private fun updateCurrentTask() {
        _uiState.value.selectedTask?.let { selectedTask ->
            viewModelScope.launch(Dispatchers.IO) {

            }
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
        pomodoroManager.stop()
        task?.let {
            pomodoroManager = getPomodoroManager(it.totalAmount - it.amountDone)
        }
    }
}
