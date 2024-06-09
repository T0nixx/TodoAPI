package com.teamsparta.todo.domain.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignUpRequestDto(
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String,
    @field:NotBlank
    val nickname: String,
)
