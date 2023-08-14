package domain.model

data class Task(
    val id: Int? = null,
    val name: String,
    val pomodoroAmount: Int,
    val isCompleted: Boolean,
)
