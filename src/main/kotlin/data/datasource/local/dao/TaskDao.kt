package data.datasource.local.dao

import data.datasource.local.dto.TaskDTO
import data.datasource.local.entity.TaskEntity
import data.datasource.local.entity.TaskTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

class TaskDao(
    private val db: Database
) : ITaskDao {

    override fun insert(task: TaskDTO): Int {
        return taskTransaction {
            TaskEntity.new {
                this.name = task.name
                this.pomodoroAmount = task.pomodoroAmount
                this.isCompleted = task.isCompleted
            }.id.value
        }
    }

    override fun update(id: Int, updatedTask: TaskDTO): TaskEntity? {
        return taskTransaction {
            val task = TaskEntity.findById(id)
            task?.apply {
                name = updatedTask.name
                pomodoroAmount = updatedTask.pomodoroAmount
                isCompleted = updatedTask.isCompleted
            }
            task
        }
    }

    override fun selectById(id: Int): TaskEntity? {
        return taskTransaction {
            TaskEntity.findById(id)
        }
    }

    override fun selectByName(name: String): List<TaskEntity> {
        return taskTransaction {
            TaskEntity.find { TaskTable.name eq name }.toList()
        }
    }

    override fun selectAll(): List<TaskEntity> {
        return taskTransaction {
            TaskEntity.all().sortedBy { it.id }.toList()
        }
    }

    override fun delete(id: Int) {
        taskTransaction {
            TaskEntity.findById(id)?.delete()
        }
    }

    private fun <T> taskTransaction(statement: Transaction.() -> T): T {
        return transaction(db, statement = statement)
    }
}
