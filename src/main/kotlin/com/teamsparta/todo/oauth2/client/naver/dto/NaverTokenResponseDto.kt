package com.teamsparta.todo.oauth2.client.naver.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverTokenResponseDto(
    @JsonProperty("access_token")
    val naverAccessToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("expires_in")
    val expiresIn: Int,
)
