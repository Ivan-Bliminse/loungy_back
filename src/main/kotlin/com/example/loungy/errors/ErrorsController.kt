package com.example.loungy.errors

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

class CustomErrorException(
    var status: HttpStatus,
    override var message: String? = null,
    val code: Int? = null
) : RuntimeException()

@ControllerAdvice
class ErrorsController {
    @ExceptionHandler
    fun handleNotFound(ex: CustomErrorException): ResponseEntity<ErrorResponse> {
        val errorMessage = ErrorResponse(
            ex.status,
            ex.message,
            ex.code
        )
        return ResponseEntity(errorMessage, ex.status)
    }
}

data class ErrorResponse(
    val status: HttpStatus?,
    val message: String?,
    val code: Int?
)