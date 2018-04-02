package de.henninglanghorst.kotlinstuff.http

import java.net.HttpURLConnection
import java.net.URL


fun json(id: Int, name: String) = """{"id": $id, "name": "$name"}"""

fun httpRequest(url: String, setup: ConnectionContext.() -> Unit) =
        HttpURLConnectionContext(URL(url).openConnection() as HttpURLConnection)
                .apply(setup)
                .httpResponse

interface ConnectionContext {
    val headers: Headers
    var requestMethod: String
    var body: String
    val httpResponse: HttpResponse

}

interface Headers {
    operator fun set(key: String, value: String)
}

