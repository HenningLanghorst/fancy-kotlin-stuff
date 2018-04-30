package de.henninglanghorst.kotlinstuff.http

import java.net.HttpURLConnection
import java.net.URL


fun json(id: Int, name: String) = """{"id": $id, "name": "$name"}"""

fun httpRequest(url: String, setup: ConnectionContext.() -> Unit) =
        URL(url).connectionContext
                .withLogging
                .apply(setup)
                .httpResponse

private val URL.connectionContext: ConnectionContext
    get() = HttpURLConnectionContext(openConnection() as HttpURLConnection)

interface ConnectionContext {
    val headers: Headers
    var requestMethod: String
    var body: String
    val httpResponse: HttpResponse

}

interface Headers {
    operator fun set(key: String, value: String)
}
