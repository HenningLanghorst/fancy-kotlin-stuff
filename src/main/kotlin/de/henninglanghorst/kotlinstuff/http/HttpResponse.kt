package de.henninglanghorst.kotlinstuff.http

sealed class HttpResponse

data class OKResponse(val statusCode: Int, val body: String) : HttpResponse()
data class ErrorResponse(val statusCode: Int, val body: String) : HttpResponse()
