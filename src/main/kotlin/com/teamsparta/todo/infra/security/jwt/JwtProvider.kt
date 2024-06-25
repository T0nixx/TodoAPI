package com.teamsparta.todo.infra.security.jwt

import com.teamsparta.todo.domain.member.model.MemberRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date

@Component
class JwtProvider(
    @Value("\${jwt.secret}")
    private val secretKey: String,
) {
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createToken(
        id: Long,
        isSocial: Boolean,
        role: MemberRole,
    ): String {
        val issuer = "todo.teamsparta.com"
        val now = Instant.now()

        val claims =
            Jwts
                .claims()
                .add(mapOf("role" to role, "isSocial" to isSocial))
                .build()
        return Jwts
            .builder()
            .signWith(key)
            .subject(id.toString())
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(86400)))
            .claims(claims)
            .compact()!!
    }

    fun validateTokenAndGetClaims(token: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
        }
    }
}
