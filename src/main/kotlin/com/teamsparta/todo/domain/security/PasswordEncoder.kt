package com.teamsparta.todo.domain.security

import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.UUID

@Component
class PasswordEncoder {
    fun encode(
        target: String,
        salt: String = UUID.randomUUID().toString(),
    ): Pair<String, String> {
        val bytes = (salt + target).toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return Pair(
            digest.fold("") { str, it -> str + "%02x".format(it) },
            salt,
        )
    }

    fun matches(target: String, salt: String, hashed: String): Boolean {
        return encode(target, salt).first == hashed
    }
}

