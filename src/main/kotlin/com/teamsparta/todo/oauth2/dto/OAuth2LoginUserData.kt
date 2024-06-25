package com.teamsparta.todo.oauth2.dto

import com.teamsparta.todo.domain.member.model.OAuth2Provider

data class OAuth2LoginUserData(
    val nickname: String,
    val provider: OAuth2Provider,
    val providerUserId: String,
)
