package com.teamsparta.todo.domain.member.model

import com.teamsparta.todo.domain.member.dto.MemberResponseDto
import com.teamsparta.todo.domain.member.dto.SignInResponseDto
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
class Member(
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
    val role: MemberRole,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun Member.toResponseDto(): MemberResponseDto {
    return MemberResponseDto(
        email = email,
        username = username,
    )
}

fun Member.toSignInResponseDto(token: String): SignInResponseDto {
    return SignInResponseDto(
        id = id!!,
        email = email,
        username = username,
        token = token,
    )
}
