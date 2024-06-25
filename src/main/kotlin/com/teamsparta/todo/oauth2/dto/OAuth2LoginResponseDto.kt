package com.teamsparta.todo.oauth2.dto

import com.teamsparta.todo.domain.member.model.OAuth2Provider

data class OAuth2LoginResponseDto(
    val provider: OAuth2Provider,
    val token: String,
)
