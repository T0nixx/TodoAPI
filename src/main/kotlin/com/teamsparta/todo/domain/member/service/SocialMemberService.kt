package com.teamsparta.todo.domain.member.service

import com.teamsparta.todo.domain.member.model.Member
import com.teamsparta.todo.domain.member.repository.MemberRepository
import com.teamsparta.todo.oauth2.dto.OAuth2LoginUserData
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SocialMemberService(
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun signInIfAbsent(userData: OAuth2LoginUserData): Member {
        val (nickname, provider, providerUserId) = userData
        return memberRepository.findByProviderAndProviderUserId(
            provider = provider,
            providerUserId = providerUserId,
        ) ?: memberRepository.save(
            Member.createSocialMember(
                provider = provider,
                providerUserId = providerUserId,
                nickname = nickname,
            ),
        )
    }
}
