package com.teamsparta.todo.domain.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignInRequestDto(
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String,
)
