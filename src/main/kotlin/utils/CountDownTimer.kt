package utils

import java.util.*

class CountDownTimer(
    durationInSeconds: Int,
    private val onTick: (Int) -> Unit,
    private val onFinish: () -> Unit
) {
    private var timer: Timer? = null
    private var remainingSeconds: Int = durationInSeconds
    private var isPaused: Boolean = false

    fun start() {
        if (timer == null) {
            timer = Timer()
            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if (!isPaused) {
                        if (remainingSeconds > 0) {
                            onTick(remainingSeconds)
                            remainingSeconds--
                        } else {
                            onFinish()
                            cancelTimer()
                        }
                    }
                }
            }, 0, 1000)
        }
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }

    fun stop() {
        cancelTimer()
    }

    private fun cancelTimer() {
        timer?.cancel()
        timer = null
        isPaused = false
    }
}