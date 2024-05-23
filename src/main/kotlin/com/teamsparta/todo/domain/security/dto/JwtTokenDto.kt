package com.teamsparta.todo.domain.security.dto

data class JwtTokenDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)
