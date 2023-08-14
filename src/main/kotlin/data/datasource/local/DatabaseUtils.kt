package data.datasource.local

import data.datasource.local.entity.TaskTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseUtils {
    private const val DRIVER_CLASS_NAME = "org.h2.Driver"
    private const val JDBC_URL = "jdbc:h2:file:./build/db"
    val database = Database.connect(JDBC_URL, DRIVER_CLASS_NAME).also {
        transaction(it) {
            SchemaUtils.create(TaskTable)
        }
    }
}
