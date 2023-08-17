package utils

enum class PomodoroEvent(val title: String) {
    FOCUS(title = "Time to focus!"),
    REST(title = "Time to grab a coffee!"),
    LONG_REST(title = "Take a longer rest now!")
}
