package com.purnendu.jsonreader.form

sealed class Response(val message: String? = null) {
    class Success(message: String) : Response(message)
    class Error(errorMessage: String) : Response(errorMessage)
    object Loading : Response()
    object Empty : Response()
}
