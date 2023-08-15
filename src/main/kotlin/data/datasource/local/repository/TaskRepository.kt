package data.datasource.local.repository

import data.datasource.local.dao.ITaskDao
import data.datasource.local.dto.TaskDTO
import data.datasource.local.entity.TaskEntity
import domain.model.Task
import domain.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TaskRepository(
    private val taskDao: ITaskDao,
) : ITaskRepository {
    override fun allTasks(): Flow<List<Task>> {
        return taskDao.selectAll().map { it.asDomain() }
    }

    override fun getTaskById(id: Int): Flow<Task?> = flow {
        emit(taskDao.selectById(id)?.asDomain())
    }

    override fun getTasksByName(name: String): Flow<List<Task>?> = flow {
        emit(taskDao.selectByName(name).asDomain())
    }

    override suspend fun updateTask(task: Task): Task? {
        task.id?.let { id ->
            return taskDao.update(
                id = id,
                updatedTask = TaskDTO(
                    name = task.name,
                    totalAmount = task.totalAmount,
                    amountDone = task.amountDone,
                    isCompleted = task.isCompleted,
                )
            )?.asDomain()
        }
        return null
    }

    override suspend fun newTask(task: Task): Int {
        return taskDao.insert(
            TaskDTO(
                name = task.name,
                totalAmount = task.totalAmount,
                amountDone = task.amountDone,
                isCompleted = task.isCompleted,
            )
        )
    }

    override suspend fun deleteTask(id: Int) {
        taskDao.delete(id)
    }

    private fun List<TaskEntity>.asDomain(): List<Task> {
        return map { it.asDomain() }
    }

    private fun TaskEntity.asDomain(): Task {
        return Task(
            id = this.id.value,
            name = this.name,
            totalAmount = this.totalAmount,
            amountDone = this.amountDone,
            isCompleted = this.isCompleted
        )
    }
}