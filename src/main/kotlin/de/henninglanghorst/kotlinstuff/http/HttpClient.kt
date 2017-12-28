package de.henninglanghorst.kotlinstuff.http

import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.streams.asSequence


fun main(args: Array<String>) {
    val post = httpRequest("https://postman-echo.com/post?hhhhh=5") {
        requestMethod = "POST"
        setRequestProperty("Authorization", "Basic d2lraTpwZWRpYQ==")
        setRequestProperty("Content-Type", "application/json; charset=utf-8")
        body = json(6, "Miller")
    }
    println(post)

    val get = httpRequest("https://postman-echo.com/get?s=5&aaa=43") {
        requestMethod = "GET"
        setRequestProperty("Authorization", "Basic d2lraTpwZWRpYQ==")
    }

    println(get)

}

private fun json(id: Int, name: String) = """{"id": ${id}, "name": "${name}"}"""

private fun httpRequest(url: String, setup: HttpURLConnection.() -> Unit) =
        URL(url).openConnection()
                .let { it as HttpURLConnection }
                .apply { doInput = true }
                .apply(setup)
                .run {
                    inputStream.bufferedReader(Charsets.UTF_8)
                            .lines()
                            .asSequence()
                            .reduce { acc, s -> acc + s }

                }


private var HttpURLConnection.body: String
    set(value) {
        doOutput = requestMethod != "GET" && value.isNotEmpty()
        if (doOutput)
            DataOutputStream(outputStream).use {
                it.writeUTF(value)
                it.flush()
            }
    }
    get() = ""
