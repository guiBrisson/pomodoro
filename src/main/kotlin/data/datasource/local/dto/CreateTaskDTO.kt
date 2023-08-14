package data.datasource.local.dto

data class CreateTaskDTO(
    val name: String,
    val pomodoroAmount: Int,
    val isCompleted: Boolean,
)
