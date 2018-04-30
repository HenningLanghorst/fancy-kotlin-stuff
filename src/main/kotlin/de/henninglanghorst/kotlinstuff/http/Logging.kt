package de.henninglanghorst.kotlinstuff.http

import org.slf4j.LoggerFactory

val ConnectionContext.withLogging: ConnectionContext get() = LoggingConnectionContext(this)

private class LoggingConnectionContext(private val base: ConnectionContext) : ConnectionContext by base {
    private val log = LoggerFactory.getLogger(ConnectionContext::class.java)
    override val headers: Headers
        get() = LoggingHeaders(base.headers)
    override var body: String
        get() = base.body
        set(value) {
            base.body = value.also { this.logRequestBody(it) }
        }

    private fun logRequestBody(requestBody: String) {
        log.debug { multilineLogMessage("Request body: ", requestBody) }
    }

    override val httpResponse: HttpResponse
        get() = base.httpResponse.also { it.logResponseBody() }

    private fun HttpResponse.logResponseBody() {
        when (this) {
            is OKResponse -> log.debug {
                "Status code  : " + this.statusCode + System.lineSeparator() +
                        multilineLogMessage("Response body: ", this.body)
            }
            is ErrorResponse -> log.error {
                "Status code  : " + this.statusCode + System.lineSeparator() +
                        multilineLogMessage("Response body: ", this.body)
            }
        }
    }

    private fun multilineLogMessage(prefix: String, multilineString: String): String {
        return multilineString.lines().let { lines ->
            if (lines.size > 1) {
                buildString {
                    appendln("$prefix${System.lineSeparator()}")
                    lines.map { "  $it" }.forEach { appendln(it) }
                }
            } else {
                "$prefix$multilineString"
            }
        }

    }

    override var requestMethod: String
        get() = base.requestMethod
        set(value) {
            log.debug { "Request method: $value" }
            base.requestMethod = value
        }

}

private class LoggingHeaders(private val base: Headers) : Headers by base {
    private val log = LoggerFactory.getLogger(Headers::class.java)
    override fun set(key: String, value: String) {
        if (log.isDebugEnabled)
            log.debug("Set header: $key = ${if (key == "Authorization") "***" else value}")
        base[key] = value
    }
}

