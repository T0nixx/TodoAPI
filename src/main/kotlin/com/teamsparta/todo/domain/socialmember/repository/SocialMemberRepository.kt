package com.teamsparta.todo.domain.socialmember.repository

import com.teamsparta.todo.domain.socialmember.model.OAuth2Provider
import com.teamsparta.todo.domain.socialmember.model.SocialMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialMemberRepository : JpaRepository<SocialMember, Long> {
    fun findByProviderAndProviderUserId(
        provider: OAuth2Provider,
        providerUserId: String,
    ): SocialMember?
}
