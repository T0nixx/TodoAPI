package com.teamsparta.todo.infra.security.jwt

import com.teamsparta.todo.domain.member.model.MemberRole
import com.teamsparta.todo.domain.socialmember.model.OAuth2Provider
import com.teamsparta.todo.infra.security.dto.MemberPrincipal
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Order(0)
@Component
class JwtAuthenticationFilter(private val jwtProvider: JwtProvider) :
    OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = parseBearerToken(request)
        if (token != null) {
            jwtProvider.validateTokenAndGetClaims(token).onSuccess { claims ->
                val memberId = claims.payload.subject.toLong()
                val role =
                    MemberRole.valueOf(claims.payload["role"] as String)
                val oAuth2Provider =
                    claims.payload["oAuth2Provider"]?.let {
                        OAuth2Provider.valueOf(
                            it as String,
                        )
                    }

                val authentication =
                    JwtAuthenticationToken(
                        principal =
                        MemberPrincipal(
                            id = memberId,
                            oAuth2Provider = oAuth2Provider,
                            roles = setOf(role),
                        ),
                        details =
                        WebAuthenticationDetailsSource().buildDetails(
                            request,
                        ),
                    )
                SecurityContextHolder.getContext().authentication =
                    authentication
            }
        }
        filterChain.doFilter(request, response)
    }

    fun parseBearerToken(request: HttpServletRequest): String? {
        return request
            .getHeader(HttpHeaders.AUTHORIZATION)
            .takeIf { it?.startsWith("Bearer ", true) ?: false }
            ?.substring(7)
    }
}
