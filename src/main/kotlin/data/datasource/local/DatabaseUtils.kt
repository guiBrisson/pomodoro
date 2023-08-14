package data.datasource.local

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseUtils {
    private val driverClassName = "org.h2.Driver"
    private val jdbcURL = "jdbc:h2:file:./build/db"
    val database = Database.connect(jdbcURL, driverClassName)

    fun createTable(table: Table) {
        transaction(database) {
            SchemaUtils.create(table)
        }
    }
}