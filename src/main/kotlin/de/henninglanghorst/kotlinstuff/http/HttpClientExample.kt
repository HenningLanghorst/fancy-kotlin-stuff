package de.henninglanghorst.kotlinstuff.http

fun main(args: Array<String>) {
    val post = httpRequest("https://postman-echo.com/post?hhhhh=5") {
        requestMethod = "POST"
        headers["Authorization"] = "Basic d2lraTpwZWRpYQ=="
        headers["Content-Type"] = "application/json; charset=utf-8"
        body = json(6, "Miller")
    }
    println(post)

    val get = httpRequest("https://postman-echo.com/status/400") {
        requestMethod = "GET"
        headers["Authorization"] = "Basic d2lraTpwZWRpYQ=="
    }
    println(get)

}

