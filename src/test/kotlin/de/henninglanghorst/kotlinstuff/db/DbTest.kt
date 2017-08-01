package de.henninglanghorst.kotlinstuff.db

import org.h2.jdbcx.JdbcDataSource
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.Month
import javax.sql.DataSource
import kotlin.test.assertEquals


class DbTest {

    var dataSource: DataSource? = null

    @Before
    fun setUp() {
        dataSource = JdbcDataSource().apply {
            setUrl("jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:create.sql'\\;RUNSCRIPT FROM 'classpath:data.sql'")
            user = "sa"
            password = "sa"
        }
    }

    @Test
    fun `Person query by single parameter should match result from Database`() {

        val databaseContext = DatabaseContext(dataSource!!)

        val uuidOfJohnDoe = "90bc1690-6218-477e-9361-47efa704f518"
        val sql = "SELECT * FROM PERSON WHERE UUID = ?" withParam uuidOfJohnDoe

        val persons = databaseContext.doInDatabase {
            query(sql) {
                resultSet ->
                Person(resultSet.getString("UUID"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getDate("BIRTHDAY")?.toLocalDate()
                )
            }
        }

        assertEquals(
                listOf(
                        Person(
                                uuid = uuidOfJohnDoe,
                                firstName = "John",
                                lastName = "Doe",
                                birthday = LocalDate.of(1977, Month.DECEMBER, 12))
                ),
                persons,
                "Person should match result from Database"
        )
    }

    @Test
    fun `Person queried by name should match result from Database`() {

        val databaseContext = DatabaseContext(dataSource!!)

        val sql = "SELECT * FROM PERSON " +
                " WHERE FIRST_NAME = ?" +
                "  AND LAST_NAME = ?" withParams listOf("Jane", "Doe")


        val persons = databaseContext.doInDatabase {
            query(sql) {
                resultSet ->
                Person(resultSet.getString("UUID"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getDate("BIRTHDAY")?.toLocalDate()
                )
            }
        }

        assertEquals(
                listOf(
                        Person(
                                uuid = "0928ecc0-9a57-4583-aee7-759dbb3352a2",
                                firstName = "Jane",
                                lastName = "Doe",
                                birthday = LocalDate.of(1979, Month.JANUARY, 22))
                ),
                persons,
                "Person should match result from Database"
        )
    }

    @Test
    fun `Person queried by name using append function should match result from Database`() {
        val databaseContext = DatabaseContext(dataSource!!)

        val sql = (("SELECT * FROM PERSON ".noParam())
                + ("WHERE FIRST_NAME = ?" withParam "Jane")
                + (" AND LAST_NAME = ?" withParam "Doe"))

        val persons = databaseContext.doInDatabase {

            query(sql) {
                resultSet ->
                Person(resultSet.getString("UUID"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getDate("BIRTHDAY")?.toLocalDate()
                )
            }
        }
        assertEquals(
                listOf(
                        Person(
                                uuid = "0928ecc0-9a57-4583-aee7-759dbb3352a2",
                                firstName = "Jane",
                                lastName = "Doe",
                                birthday = LocalDate.of(1979, Month.JANUARY, 22))
                ),
                persons,
                "Person should match result from Database"
        )
    }
}