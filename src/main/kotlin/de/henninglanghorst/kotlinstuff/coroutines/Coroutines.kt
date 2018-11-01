package de.henninglanghorst.kotlinstuff.coroutines


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors


private val logger = LoggerFactory.getLogger(Exception().stackTrace[0].className)

fun main(args: Array<String>) {


    val message1 = httpRequestAsync("https://postman-echo.com/post?hhhhh=5") {
        requestMethod = "POST"
        setRequestProperty("Authorization", "Basic d2lraTpwZWRpYQ==")
        setRequestProperty("Content-Type", "application/json; charset=utf-8")
        body = json(6, "Miller")
    }

    val message2 = httpRequestAsync("https://postman-echo.com/get?s=5&aaa=43") {
        requestMethod = "GET"
        setRequestProperty("Authorization", "Basic d2lraTpwZWRpYQ==")
    }

    val message3 = httpRequestAsync("https://postman-echo.com/status/400") {
        requestMethod = "GET"
        setRequestProperty("Authorization", "Basic d2lraTpwZWRpYQ==")
    }

    runBlocking {
        println(message1.await())
        println(message2.await())
    }
    runBlocking { println(message3.await()) }


}

private fun json(id: Int, name: String) = """{"id": $id, "name": "$name"}"""

private fun httpRequestAsync(url: String, setup: HttpURLConnection.() -> Unit) = GlobalScope.async { httpRequest(url, setup) }

private fun httpRequest(url: String, setup: HttpURLConnection.() -> Unit): String {
    return URL(url).openConnection()
            .let { it as HttpURLConnection }
            .apply { doInput = true }
            .apply(setup)
            .also { logger.info("HTTP ${it.requestMethod} call to $url") }
            .run {
                responseStream.bufferedReader(Charsets.UTF_8)
                        .lines()
                        .collect(Collectors.joining(System.lineSeparator()))
            }
}

private val HttpURLConnection.responseStream: InputStream
    get() =
        try {
            inputStream
        } catch (e: IOException) {
            errorStream ?: ByteArrayInputStream(byteArrayOf())
        }

private var HttpURLConnection.body: String
    set(value) {
        doOutput = requestMethod != "GET" && value.isNotEmpty()
        if (doOutput)
            PrintStream(outputStream).use {
                it.println(value)
                it.flush()
            }
    }
    get() = ""


private fun return5(): Int {
    Thread.sleep(4000)
    return 5
}