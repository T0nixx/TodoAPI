package com.teamsparta.todo.infra.security.dto

data class JwtTokenDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)
