package com.teamsparta.todo.oauth2.service

import com.teamsparta.todo.domain.member.model.OAuth2Provider
import com.teamsparta.todo.oauth2.client.OAuth2Client
import com.teamsparta.todo.oauth2.dto.OAuth2LoginUserData
import org.springframework.stereotype.Service

@Service
class OAuth2ClientService(private val clients: List<OAuth2Client>) {
    fun getAuthorizationUrl(provider: OAuth2Provider): String? {
        return selectClient(provider).getAuthorizationUrl()
    }

    fun login(
        provider: OAuth2Provider,
        authorizationCode: String,
    ): OAuth2LoginUserData {
        val client = selectClient(provider)
        return client
            .getAccessToken(authorizationCode)
            .let { client.getUserData(it) }
    }

    private fun selectClient(provider: OAuth2Provider): OAuth2Client {
        return clients.find { it.support(provider) }
            ?: throw IllegalArgumentException(
                "Provider $provider is not supported",
            )
    }
}
