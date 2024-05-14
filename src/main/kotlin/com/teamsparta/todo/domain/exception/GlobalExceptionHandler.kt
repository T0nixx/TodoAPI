package com.teamsparta.todo.domain.exception

import com.teamsparta.todo.domain.exception.dto.ErrorResponse
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e: ModelNotFoundException): ErrorResponse {
        return ErrorResponse(message = e.message)
    }
}
