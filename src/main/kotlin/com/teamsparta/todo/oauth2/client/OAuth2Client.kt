package com.teamsparta.todo.oauth2.client

import com.teamsparta.todo.domain.socialmember.model.OAuth2Provider
import com.teamsparta.todo.oauth2.dto.OAuth2LoginUserData

interface OAuth2Client {
    fun getAuthorizationUrl(): String
    fun getAccessToken(authorizationCode: String): String
    fun getUserData(
        accessToken: String,
    ): OAuth2LoginUserData

    fun support(provider: OAuth2Provider): Boolean
}
