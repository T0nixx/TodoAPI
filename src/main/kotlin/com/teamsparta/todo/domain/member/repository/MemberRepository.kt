package com.teamsparta.todo.domain.member.repository

import com.teamsparta.todo.domain.member.model.Member
import com.teamsparta.todo.domain.member.model.OAuth2Provider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Member?

    fun findByProviderAndProviderUserId(
        provider: OAuth2Provider,
        providerUserId: String,
    ): Member?
}
