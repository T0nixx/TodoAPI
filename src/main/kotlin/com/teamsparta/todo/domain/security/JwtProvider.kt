package com.teamsparta.todo.domain.security

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@Component
class JwtProvider(
    @Value("\${jwt.secret}")
    private val secretKey: String,
) {
    val key = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")

    fun createToken(userSpecification: String): String {
        return Jwts
            .builder()
            .signWith(key)
            .subject(userSpecification)
            .issuer("TONIXX")
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusSeconds(86400)))
            .compact()!!
    }

    fun validateTokenAndGetSubject(token: String): String {
        return Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token).payload.subject
    }
}

