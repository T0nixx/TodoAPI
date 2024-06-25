package com.teamsparta.todo.oauth2.client.naver

import com.teamsparta.todo.domain.member.model.OAuth2Provider
import com.teamsparta.todo.oauth2.client.OAuth2Client
import com.teamsparta.todo.oauth2.client.naver.dto.NaverTokenResponseDto
import com.teamsparta.todo.oauth2.client.naver.dto.NaverUserDataDto
import com.teamsparta.todo.oauth2.client.naver.dto.NaverUserResponseDto
import com.teamsparta.todo.oauth2.dto.OAuth2LoginUserData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder

@Component
class NaverOAuth2Client(
    @Value("\${naver-oauth.clientId}")
    val clientId: String,
    @Value("\${naver-oauth.clientSecret}")
    val clientSecret: String,
    @Value("\${naver-oauth.redirectUri}")
    val redirectUri: String,
    @Value("\${naver-oauth.authBaseUrl}")
    val authBaseUrl: String,
    @Value("\${naver-oauth.userDataUrl}")
    val userDataUrl: String,
    private val restClient: RestClient,
) : OAuth2Client {
    companion object {
        private const val PROVIDER = "NAVER"
        private const val STATE = "NAVER-OAUTH-STATE"
    }

    override fun getAuthorizationUrl(): String {
        val uriComponents =
            UriComponentsBuilder
                .fromUriString("$authBaseUrl/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam(
                    "redirect_uri",
                    URLEncoder.encode(redirectUri, "UTF-8"),
                )
                .queryParam("state", URLEncoder.encode(STATE, "UTF-8"))
                .build()
        return uriComponents.toString()
    }

    override fun getAccessToken(authorizationCode: String): String {
        val uriComponents =
            UriComponentsBuilder.fromUriString("$authBaseUrl/token")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("grant_type", "authorization_code")
                .queryParam("state", URLEncoder.encode(STATE, "UTF-8"))
                .queryParam("code", authorizationCode)
                .build()
        val getTokenUrl = uriComponents.toUriString()
        return restClient
            .get()
            .uri(getTokenUrl)
            .retrieve()
            .body<NaverTokenResponseDto>()?.naverAccessToken
            ?: throw RuntimeException("Get Naver token failed.")
    }

    override fun getUserData(accessToken: String): OAuth2LoginUserData {
        val (id, nickname) =
            restClient
                .get()
                .uri(userDataUrl)
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .body<NaverUserResponseDto<NaverUserDataDto>>()?.response
                ?: throw RuntimeException("Get Naver user data failed.")

        return OAuth2LoginUserData(
            nickname = nickname,
            provider = OAuth2Provider.NAVER,
            providerUserId = id,
        )
    }

    override fun support(provider: OAuth2Provider): Boolean {
        return PROVIDER == provider.name
    }
}
