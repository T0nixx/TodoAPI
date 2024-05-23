package com.teamsparta.todo.domain.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetails
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
        val user = parseUserSpecification(token)
        val authenticated =
            UsernamePasswordAuthenticationToken.authenticated(
                user,
                token,
                user.authorities,
            )
        authenticated.details = WebAuthenticationDetails(request)
        SecurityContextHolder.getContext().authentication = authenticated

        filterChain.doFilter(request, response)
    }

    fun parseBearerToken(request: HttpServletRequest): String? {
        return request
            .getHeader(HttpHeaders.AUTHORIZATION)
            .takeIf { it?.startsWith("Bearer ", true) ?: false }
            ?.substring(7)
    }

    fun parseUserSpecification(token: String?): User {
        val subject = if (token == null) "anonymous::anonymous"
        else jwtProvider.validateTokenAndGetSubject(token)

        return subject
            .split(":")
            .let { User(it[0], it[1], listOf(SimpleGrantedAuthority(it[2]))) }
    }
}
