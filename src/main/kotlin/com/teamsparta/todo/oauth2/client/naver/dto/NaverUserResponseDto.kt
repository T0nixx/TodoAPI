package com.teamsparta.todo.oauth2.client.naver.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverUserResponseDto<T>(
    @JsonProperty("resultcode")
    val resultCode: String,
    val message: String,
    val response: T,
)
