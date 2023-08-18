package utils

class PomodoroManager(private val sessions: Int) {
    private var eventSessions: List<PomodoroEvent> = calculateEventSession()
    private var currentEventIndex: Int = 0
    private var timer: CountDownTimer? = null

    var onTick: ((Int) -> Unit)? = null
    var onEvent: ((PomodoroEvent) -> Unit)? = null
    var onPomodoroFinish: (() -> Unit)? = null

    init {
        if (sessions > 0) {
            timer = setupTimer(eventSessions[currentEventIndex])
        }
    }

    private fun calculateEventSession(): List<PomodoroEvent> {
        val sessionList = mutableListOf<PomodoroEvent>()
        for (session in (sessions * 2) downTo 1) {
            val event: PomodoroEvent = when {
                session % 8 == 0 -> PomodoroEvent.LONG_REST // long rest after every 4 focus sessions
                session % 2 == 0 -> PomodoroEvent.REST // rest after focus sessions
                else -> PomodoroEvent.FOCUS
            }
            sessionList.add(event)
        }


        if (sessions > 0) {
            // removing last session if it's not PomodoroEvent.FOCUS
            if (sessionList.size % 2 == 0) {
                sessionList.removeFirst()
            }

            updateOnEvent(sessionList.first())
        }

        return sessionList.reversed()

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

    fun nextSection() {
        stop() // stop current timer
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

    fun startTimer() {
        timer?.start()
    }

    fun stop() {
        timer?.stop()
        timer = null
    }

    fun pause() {
        timer?.pause()
    }

    fun resume() {
        timer?.resume()
    }

}
