package de.henninglanghorst.kotlinstuff.coroutines


import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors


fun main(args: Array<String>) {


    runBlocking {

        println(httpRequest("https://postman-echo.com/post?hhhhh=5") {
            requestMethod = "POST"
            setRequestProperty("Authorization", "Basic d2lraTpwZWRpYQ==")
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
            body = json(6, "Miller")
        })

        println(httpRequest("https://postman-echo.com/get?s=5&aaa=43") {
            requestMethod = "GET"
            setRequestProperty("Authorization", "Basic d2lraTpwZWRpYQ==")
        })

        println(httpRequest("https://postman-echo.com/status/400") {
            requestMethod = "GET"
            setRequestProperty("Authorization", "Basic d2lraTpwZWRpYQ==")
        })


    }


}

private fun json(id: Int, name: String) = """{"id": $id, "name": "$name"}"""

private suspend fun httpRequest(url: String, setup: HttpURLConnection.() -> Unit) =
        async(CommonPool) {
            URL(url).openConnection()
                    .let { it as HttpURLConnection }
                    .apply { doInput = true }
                    .apply(setup)
                    .run {
                        responseStream.bufferedReader(Charsets.UTF_8)
                                .lines()
                                .collect(Collectors.joining(System.lineSeparator()))
                    }
        }.await()

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
