package data.datasource.local.di

import data.datasource.local.DatabaseUtils
import data.datasource.local.dao.ITaskDao
import data.datasource.local.dao.TaskDao
import data.datasource.local.repository.TaskRepository
import domain.repository.ITaskRepository
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val localDataModule = module {
    single<Database> { DatabaseUtils.database }
    single<ITaskDao> { TaskDao(get()) }
    single<ITaskRepository> { TaskRepository(get()) }
}
