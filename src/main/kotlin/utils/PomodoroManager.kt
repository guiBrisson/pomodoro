package utils

class PomodoroManager(private val sessions: Int) {
    private var eventSessions: List<PomodoroEvent> = calculateEventSession()
    private var currentEventIndex: Int = 0
    private var timer: CountDownTimer = setupTimer(eventSessions[currentEventIndex])

    var onTick: ((Int) -> Unit)? = null
    var onEvent: ((PomodoroEvent) -> Unit)? = null
    var onPomodoroFinish: (() -> Unit)? = null

    private fun calculateEventSession(): List<PomodoroEvent> {
        val sessionList = mutableListOf<PomodoroEvent>()
        for (session in sessions downTo 1) {
            val event: PomodoroEvent = when {
                session % 8 == 0 -> PomodoroEvent.LONG_REST // long rest after every 4 focus sessions
                session % 2 == 0 -> PomodoroEvent.REST // rest after focus sessions
                else -> PomodoroEvent.FOCUS
            }
            sessionList.add(event)
        }
        val reversedList = sessionList.reversed()
        updateOnEvent(reversedList.first())

        return reversedList
    }

    private fun updateOnEvent(event: PomodoroEvent) = onEvent?.invoke(event)

    private fun setupTimer(event: PomodoroEvent): CountDownTimer {
        val duration = when (event) {
            PomodoroEvent.FOCUS -> PomodoroSettings.focusTimeInSeconds
            PomodoroEvent.REST -> PomodoroSettings.restTimeInSeconds
            PomodoroEvent.LONG_REST -> PomodoroSettings.longRestTimeInSeconds
        }
        return CountDownTimer(
            durationInSeconds = duration,
            onTick = { onTick?.invoke(it) },
            onFinish = this::nextSection
        )
    }

    fun startTimer() {
        timer.start()
    }

    fun nextSection() {
        timer.stop() // stop current timer
        if (currentEventIndex < eventSessions.size - 1) {
            currentEventIndex++
            val nextEvent = eventSessions[currentEventIndex]
            timer = setupTimer(nextEvent)

            startTimer()
            updateOnEvent(nextEvent)
        } else run {
            onPomodoroFinish?.invoke()
        }
    }

    fun pause() {
        timer.pause()
    }

    fun resume() {
        timer.resume()
    }

}
