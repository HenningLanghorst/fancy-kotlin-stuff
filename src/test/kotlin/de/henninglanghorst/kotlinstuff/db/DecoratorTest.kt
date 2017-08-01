package de.henninglanghorst.kotlinstuff.db

import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import java.sql.Connection

class DecoratorTest {

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var connection: Connection

    @Test
    fun name() {
        // given
        val nonCommittingConnection = NonCommittingConnection(connection)
        // when
        nonCommittingConnection.commit();
        // then
        verify(connection, never()).commit()
    }
}


class NonCommittingConnection(val delegate: Connection) : Connection by delegate {

    override fun commit() {
        println("Commit suppressed");
    }
}