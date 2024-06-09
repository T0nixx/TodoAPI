package com.teamsparta.todo.domain.socialmember.service

import com.teamsparta.todo.domain.member.model.MemberRole
import com.teamsparta.todo.domain.socialmember.model.SocialMember
import com.teamsparta.todo.domain.socialmember.repository.SocialMemberRepository
import com.teamsparta.todo.oauth2.dto.OAuth2LoginUserData
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SocialMemberService(private val socialMemberRepository: SocialMemberRepository) {
    @Transactional
    fun signInIfAbsent(
        userData: OAuth2LoginUserData,
    ): SocialMember {
        val (nickname, provider, providerUserId) = userData
        return socialMemberRepository.findByProviderAndProviderUserId(
            provider = provider,
            providerUserId = providerUserId,
        ) ?: socialMemberRepository.save(
            SocialMember(
                provider = provider,
                providerUserId = providerUserId,
                role = MemberRole.USER,
                nickname = nickname,
            ),
        )
    }
}
