package domain.model

data class Task(
    val id: Int? = null,
    val name: String,
    val totalAmount: Int,
    val amountDone: Int,
    val isCompleted: Boolean,
)
