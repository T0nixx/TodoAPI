package com.teamsparta.todo.domain.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignUpRequestDto(
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String,
    @field:NotBlank
    val username: String,
)
