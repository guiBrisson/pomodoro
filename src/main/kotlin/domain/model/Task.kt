package domain.model

data class Task(
    val id: Int?,
    val name: String,
    val totalAmount: Int,
    val amountDone: Int,
    val isCompleted: Boolean,
) {
    constructor(name: String, totalAmount: Int) : this(
        null,
        name,
        totalAmount,
        0,
        false
    )

    fun amountDone(): Int {
        return (amountDone + 1) / 2
    }

    fun totalAmount(): Int {
        return totalAmount / 2
    }
}
