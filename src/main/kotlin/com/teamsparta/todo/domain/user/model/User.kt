package com.teamsparta.todo.domain.user.model

import com.teamsparta.todo.domain.user.dto.SignInResponseDto
import com.teamsparta.todo.domain.user.dto.UserResponseDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "app_user")
class User(
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "salt", nullable = false, updatable = false, unique = true)
    val salt: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun User.toResponseDto(): UserResponseDto {
    return UserResponseDto(
        email = email,
        username = username,
    )
}

fun User.toSignInResponseDto(token: String): SignInResponseDto {
    return SignInResponseDto(
        id = id!!,
        email = email,
        username = username,
        token = token,
    )
}
