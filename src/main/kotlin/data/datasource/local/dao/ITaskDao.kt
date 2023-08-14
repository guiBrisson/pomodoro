package data.datasource.local.dao

import data.datasource.local.dto.CreateTaskDTO
import data.datasource.local.entity.TaskEntity

interface ITaskDao {
    fun insert(task: CreateTaskDTO): Int
    fun update(id: Int): TaskEntity
    fun selectById(id: Int): TaskEntity?
    fun selectByName(name: String): List<TaskEntity>
    fun selectAll(): List<TaskEntity>
    fun delete(id: Int)
}
