package com.teamsparta.todo.domain.socialmember.model

import com.teamsparta.todo.domain.member.model.MemberRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "social_member")
class SocialMember(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: OAuth2Provider,
    @Column(nullable = false)
    val providerUserId: String,
    @Column(nullable = false)
    var nickname: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: MemberRole,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
