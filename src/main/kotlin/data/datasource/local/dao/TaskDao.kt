package data.datasource.local.dao

import data.datasource.local.dto.TaskDTO
import data.datasource.local.entity.TaskEntity
import data.datasource.local.entity.TaskTable
import kotlinx.coroutines.flow.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

class TaskDao(
    private val db: Database
) : ITaskDao {
    private val _allTasks: MutableStateFlow<List<TaskEntity>> = MutableStateFlow(allTasks())
    override fun selectAll(): StateFlow<List<TaskEntity>> = _allTasks.asStateFlow()


    override fun insert(task: TaskDTO): Int {
        return taskTransaction {
            TaskEntity.new {
                this.name = task.name
                this.totalAmount = task.totalAmount
                this.amountDone = task.amountDone
                this.isCompleted = task.isCompleted
            }.id.value
        }.also { _allTasks.update { allTasks() } }
    }

    override fun update(id: Int, updatedTask: TaskDTO): TaskEntity? {
        return taskTransaction {
            val task = TaskEntity.findById(id)
            task?.apply {
                name = updatedTask.name
                totalAmount = updatedTask.totalAmount
                amountDone = updatedTask.amountDone
                isCompleted = updatedTask.isCompleted
            }
            task
        }.also { _allTasks.update { allTasks() } }
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

    override fun delete(id: Int) {
        taskTransaction {
            TaskEntity.findById(id)?.delete()
        }
        _allTasks.update { allTasks() }
    }

    private fun allTasks(): List<TaskEntity> {
        return taskTransaction {
            TaskEntity.all().sortedBy { it.id }.toList()
        }
    }

    private fun <T> taskTransaction(statement: Transaction.() -> T): T {
        return transaction(db, statement = statement)
    }
}
