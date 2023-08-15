package data.datasource.local.dao

import data.datasource.local.dto.TaskDTO
import data.datasource.local.entity.TaskEntity
import kotlinx.coroutines.flow.StateFlow

interface ITaskDao {
    fun insert(task: TaskDTO): Int
    fun update(id: Int, updatedTask: TaskDTO): TaskEntity?
    fun selectById(id: Int): TaskEntity?
    fun selectByName(name: String): List<TaskEntity>
    fun selectAll(): StateFlow<List<TaskEntity>>
    fun delete(id: Int)
    fun deleteAll()
    fun deleteCompletedTasks()
}
