package com.teamsparta.todo.domain.user.dto

data class SignUpRequestDto(
    val email: String,
    val password: String,
    val username: String,
)
