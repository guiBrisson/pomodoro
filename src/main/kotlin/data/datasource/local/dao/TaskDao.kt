package data.datasource.local.dao

import data.datasource.local.DatabaseUtils
import data.datasource.local.dto.CreateTaskDTO
import data.datasource.local.entity.TaskEntity
import data.datasource.local.entity.TaskTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

class TaskDao {
    private val db = DatabaseUtils.database

    init {
        DatabaseUtils.createTable(TaskTable)
    }

    fun insert(task: CreateTaskDTO): Int {
        return taskTransaction {
            TaskEntity.new {
                this.name = task.name
                this.pomodoroAmount = task.pomodoroAmount
                this.isCompleted = task.isCompleted
            }.id.value
        }
    }

    fun update(id: Int): TaskEntity {
        TODO()
    }

     fun selectById(id: Int): TaskEntity? {
        return taskTransaction {
            TaskEntity.findById(id)
        }
    }

    fun selectByName(name: String): List<TaskEntity> {
        return taskTransaction {
            TaskEntity.find { TaskTable.name eq name }.toList()
        }
    }

    fun selectAll(): List<TaskEntity> {
        return taskTransaction {
            TaskEntity.all().sortedBy { it.id }.toList()
        }
    }

    fun delete(id: Int) {
        taskTransaction {
            TaskEntity.findById(id)?.delete()
        }
    }

    private fun <T> taskTransaction(statement: Transaction.() -> T): T {
        return transaction(db, statement = statement)
    }
}
