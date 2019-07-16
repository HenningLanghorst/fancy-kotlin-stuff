package de.henninglanghorst.kotlinstuff.db

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.sql.Connection

@ExtendWith(MockitoExtension::class)
class DecoratorTest {

    @Mock
    private lateinit var connection: Connection

    @Test
    fun name() {
        // given
        val nonCommittingConnection = NonCommittingConnection(connection)
        // when
        nonCommittingConnection.commit()
        // then
        verify(connection, never()).commit()
    }
}


class NonCommittingConnection(val delegate: Connection) : Connection by delegate {

    override fun commit() {
        println("Commit suppressed")
    }
}