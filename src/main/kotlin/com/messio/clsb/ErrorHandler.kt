package com.messio.clsb

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest

@RestControllerAdvice
class ErrorHandler {
    @ExceptionHandler(ServletException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleServletException(e: ServletException, req: HttpServletRequest): Map<String, String> {
        val map = mutableMapOf("error" to "error_bad_credentials")
        e.message?.let { map["error_description"] = it }
        req.getParameter("state")?.let { map["state"] = it }
        return map
    }

    @ExceptionHandler(Throwable::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleThrowable(t: Throwable, req: HttpServletRequest): Map<String, String>{
        val map = mutableMapOf("error" to "error_invalid_request")
        t.message?.let { map["error_description"] = it }
        req.getParameter("state")?.let { map["state"] = it }
        return map
    }
}