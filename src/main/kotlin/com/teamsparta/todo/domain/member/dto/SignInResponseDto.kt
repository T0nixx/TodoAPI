package com.teamsparta.todo.domain.member.dto

data class SignInResponseDto(
    val memberId: Long,
    val token: String,
)
