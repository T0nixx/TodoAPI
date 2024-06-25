package com.teamsparta.todo.oauth2.service

import com.teamsparta.todo.domain.member.model.OAuth2Provider
import com.teamsparta.todo.domain.member.service.SocialMemberService
import com.teamsparta.todo.infra.security.jwt.JwtProvider
import com.teamsparta.todo.oauth2.dto.OAuth2LoginResponseDto
import org.springframework.stereotype.Service

@Service
class OAuth2LoginService(
    private val socialMemberService: SocialMemberService,
    private val oAuth2ClientService: OAuth2ClientService,
    private val jwtProvider: JwtProvider,
) {
    fun login(
        provider: OAuth2Provider,
        authorizationCode: String,
    ): OAuth2LoginResponseDto {
        return oAuth2ClientService.login(provider, authorizationCode)
            .let { socialMemberService.signInIfAbsent(it) }
            .let {
                jwtProvider.createToken(
                    id = it.id!!,
                    isSocial = true,
                    role = it.role,
                )
            }
            .let {
                OAuth2LoginResponseDto(provider = provider, token = it)
            }
    }
}
