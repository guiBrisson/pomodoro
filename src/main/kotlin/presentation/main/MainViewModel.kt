package presentation.main

import core.viewmodel.ViewModel
import domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import utils.CountDownTimer

class MainViewModel : ViewModel() {
    private var timer: CountDownTimer? = null

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun handleEvents(event: MainEvent) {
        when (event) {
            is MainEvent.SetupTimer -> setUpTimer(event.durationOnSeconds)
            MainEvent.ResumeTimer -> resumeTimer()
            MainEvent.PauseTimer -> pauseTimer()
            MainEvent.StopTimer -> stopTimer()
        }
    }

    private fun setUpTimer(durationInSeconds: Int) {
        timer = CountDownTimer(
            durationInSeconds,
            onTick = { seconds ->
                _uiState.update { it.copy(timer = seconds) }
            },
            onFinish = { _uiState.update { it.copy(timer = 0) } }
        )
        startTimer()
    }

    private fun startTimer() {
        timer?.start()
        _uiState.update { it.copy(isTimerRunning = true) }
    }

    private fun stopTimer() {
        timer?.stop()
        _uiState.update { it.copy(timer = 0, isTimerRunning = false) }
    }

    private fun pauseTimer() {
        timer?.pause()
        _uiState.update { it.copy(isTimerRunning = false) }
    }

    private fun resumeTimer() {
        timer?.resume()
        _uiState.update { it.copy(isTimerRunning = true) }
    }

    fun setSelectedTask(task: Task?) {
        _uiState.update { it.copy(selectedTask = task) }
    }

    override fun onDispose() {
        super.onDispose()
        timer?.stop()
    }

}
