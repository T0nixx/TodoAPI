package com.teamsparta.todo.domain.member.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "member")
class Member(
    @Column(name = "email", unique = true)
    val email: String?,
    @Column(name = "nickname", nullable = false)
    val nickname: String,
    @Column(name = "password")
    val password: String?,
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: MemberRole,
    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    val provider: OAuth2Provider?,
    @Column(name = "provider_user_id")
    val providerUserId: String?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun createMember(
            email: String,
            nickname: String,
            password: String,
        ): Member {
            return Member(
                email = email,
                nickname = nickname,
                password = password,
                role = MemberRole.USER,
                provider = null,
                providerUserId = null,
            )
        }

        fun createSocialMember(
            nickname: String,
            provider: OAuth2Provider,
            providerUserId: String,
        ): Member {
            return Member(
                email = null,
                nickname = nickname,
                password = null,
                role = MemberRole.USER,
                provider = provider,
                providerUserId = providerUserId,
            )
        }
    }
}
