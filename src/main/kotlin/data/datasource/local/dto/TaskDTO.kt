package data.datasource.local.dto

data class TaskDTO(
    val name: String,
    val totalAmount: Int,
    val amountDone: Int,
    val isCompleted: Boolean,
)
