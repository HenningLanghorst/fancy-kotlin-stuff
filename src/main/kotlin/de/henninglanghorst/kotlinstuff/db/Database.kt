package de.henninglanghorst.kotlinstuff.db

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDate
import javax.sql.DataSource


typealias UUID = String


class DatabaseContext(private val dataSource: DataSource) {
    fun <T> query(sql: SQL, mapResultSet: (ResultSet) -> T): List<T> {
        dataSource.connection.use {
            sql.prepareStatement(it).use {
                it.executeQuery().use { resultSet ->
                    val result = mutableListOf<T>()
                    while (resultSet.next()) {
                        result.add(mapResultSet(resultSet));
                    }
                    return result
                }
            }
        }
    }

    fun <T> doInDatabase(func: DatabaseContext.() -> T): T = this.func()

}


data class SQL(val sql: String, val params: List<Any> = emptyList()) {


    fun prepareStatement(connection: Connection): PreparedStatement {
        return connection.prepareStatement(sql).apply {
            for (i in 1..params.size) {
                setObject(i, params[i - 1])
            }
        }
    }

    operator fun plus(other: SQL): SQL = SQL(this.sql + " " + other.sql, this.params + other.params)

}

infix fun String.withParams(params: List<Any>) = SQL(this, params)
infix fun String.withParam(param: Any) = SQL(this, listOf(param))
fun String.noParam() = SQL(this)


data class Person(val uuid: UUID, val firstName: String, val lastName: String, val birthday: LocalDate?)
