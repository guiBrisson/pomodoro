package data.datasource.local.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TaskTable: IntIdTable() {
    val name: Column<String> = varchar("name", 360)
    val pomodoroAmount: Column<Int> = integer("pomodoro_amount")
    val isCompleted: Column<Boolean> = bool("is_completed")
}

class TaskEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<TaskEntity>(TaskTable)

    var name by TaskTable.name
    var pomodoroAmount by TaskTable.pomodoroAmount
    var isCompleted by TaskTable.isCompleted
}
