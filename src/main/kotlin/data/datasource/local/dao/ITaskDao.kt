package data.datasource.local.dao

import data.datasource.local.dto.TaskDTO
import data.datasource.local.entity.TaskEntity

interface ITaskDao {
    fun insert(task: TaskDTO): Int
    fun update(id: Int, updatedTask: TaskDTO): TaskEntity?
    fun selectById(id: Int): TaskEntity?
    fun selectByName(name: String): List<TaskEntity>
    fun selectAll(): List<TaskEntity>
    fun delete(id: Int)
}
