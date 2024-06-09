package com.teamsparta.todo.domain.member.dto

data class SignInResponseDto(
    val id: Long,
    val email: String,
    val username: String,
    val token: String,
)
