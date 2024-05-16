package com.teamsparta.todo.domain.security

import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class PasswordEncoder {
    fun encode(target: String, salt: String): String {
        val bytes = (salt + target).toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun matches(target: String, salt: String, hashed: String): Boolean {
        return encode(target, salt) == hashed
    }
}

