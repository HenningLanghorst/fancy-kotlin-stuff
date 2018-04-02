package de.henninglanghorst.kotlinstuff.http

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.net.HttpURLConnection
import java.util.stream.Collectors
import kotlin.reflect.KProperty

class HttpURLConnectionContext(private val connection: HttpURLConnection) : ConnectionContext {
    init {
        connection.doInput = true
    }

    override val headers: Headers = HttpURLConnectionRequestPropeties()
    override var requestMethod: String by JavaProperty(connection::getRequestMethod, connection::setRequestMethod)
    override var body: String
        set(value) {
            with(connection) {
                doOutput = requestMethod != "GET" && value.isNotEmpty()
                if (doOutput) {
                    outputStream.writeString(value)
                }
            }
        }
        get() = throw UnsupportedOperationException()


    override val httpResponse: HttpResponse
        get() =
            with(connection)
            {
                try {
                    OKResponse(responseCode, inputStream.stringValue)
                } catch (e: IOException) {
                    ErrorResponse(responseCode, errorStream?.stringValue ?: "")
                }
            }

    private inner class HttpURLConnectionRequestPropeties : Headers {
        override operator fun set(key: String, value: String) = connection.setRequestProperty(key, value)
    }
}


private class JavaProperty<T>(private val get: () -> T, private val set: (T) -> Unit) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        set(value)
    }
}


private fun OutputStream.writeString(value: String) {
    PrintStream(this).use {
        it.println(value)
        it.flush()
    }
}

private val InputStream.stringValue: String
    get() = bufferedReader(Charsets.UTF_8)
            .lines()
            .collect(Collectors.joining(System.lineSeparator()))

