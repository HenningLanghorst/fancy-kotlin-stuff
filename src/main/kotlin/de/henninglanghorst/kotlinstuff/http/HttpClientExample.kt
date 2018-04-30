package de.henninglanghorst.kotlinstuff.http

import org.slf4j.Logger
import org.slf4j.LoggerFactory


val log: Logger = LoggerFactory.getLogger(Exception().stackTrace[0].className)

fun main(args: Array<String>) {

    val post = httpRequest("https://postman-echo.com/post?hhhhh=5") {
        requestMethod = "POST"
        headers["Authorization"] = "Basic d2lraTpwZWRpYQ=="
        headers["Content-Type"] = "application/json; charset=utf-8"
        body = json(6, "Miller")
    }
    log.info("POST result {}", post)

    val get = httpRequest("https://postman-echo.com/status/400") {
        requestMethod = "GET"
        headers["Authorization"] = "Basic d2lraTpwZWRpYQ=="
    }
    log.info("GET result {}", get)

}

