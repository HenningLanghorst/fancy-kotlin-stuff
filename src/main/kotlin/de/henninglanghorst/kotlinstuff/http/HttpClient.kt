package de.henninglanghorst.kotlinstuff.http

import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.streams.asSequence


fun main(args: Array<String>) {
    val post = post(
            "https://postman-echo.com/post?hhhhh=5",
            "Basic d2lraTpwZWRpYQ==",
            json(6, "Miller"))
    println(post)

    val get = get("https://postman-echo.com/get?s=5&aaa=43", "Basic d2lraTpwZWRpYQ==")

    println(get)
}

private fun json(id: Int, name: String) = """{"id": ${id}, "name": "${name}"}"""

private fun post(url: String, authorization: String? = null, json: String = "") =
        URL(url).openConnection()
                .let { it as HttpURLConnection }
                .run {
                    requestMethod = "POST"
                    doOutput = true
                    doInput = true
                    authorization?.run { setRequestProperty("Authorization", authorization) }
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")

                    DataOutputStream(outputStream).use {
                        it.writeUTF(json)
                        it.flush()
                    }
                    inputStream.bufferedReader(Charsets.UTF_8)
                            .lines()
                            .asSequence()
                            .reduce { acc, s -> acc + s }
                }

private fun get(url: String, authorization: String? = null) =
        URL(url).openConnection()
                .let { it as HttpURLConnection }
                .run {
                    requestMethod = "GET"
                    doOutput = false
                    doInput = true
                    authorization?.run { setRequestProperty("Authorization", authorization) }
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")

                    inputStream.bufferedReader(Charsets.UTF_8)
                            .lines()
                            .asSequence()
                            .reduce { acc, s -> acc + s }
                }

