package domain.model

data class Task(
    val id: Int?,
    val name: String,
    val totalAmount: Int,
    val amountDone: Int,
    val isCompleted: Boolean,
) {
    constructor(name: String, totalAmount: Int): this(
        null,
        name = name,
        totalAmount = totalAmount,
         0,
         false,
    )
}
