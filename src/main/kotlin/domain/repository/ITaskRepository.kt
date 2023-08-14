package domain.repository

import domain.model.Task
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {
    fun allTasks(): Flow<List<Task>>
    fun getTaskById(id: Int): Flow<Task?>
    fun getTasksByName(name: String): Flow<List<Task>?>
    suspend fun updateTask(task: Task): Task?
    suspend fun newTask(task: Task): Int
    suspend fun deleteTask(id: Int)
}
