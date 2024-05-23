package com.teamsparta.todo.domain.user.dto

data class SignInResponseDto(
    val id: Long,
    val email: String,
    val username: String,
    val token: String,
)
