package com.teamsparta.todo.infra.security.dto

import com.teamsparta.todo.domain.member.model.MemberRole
import com.teamsparta.todo.domain.socialmember.model.OAuth2Provider
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class MemberPrincipal(
    val id: Long,
    val oAuth2Provider: OAuth2Provider?,
    val authorities: Collection<GrantedAuthority>,
) {
    constructor(
        id: Long,
        oAuth2Provider: OAuth2Provider?,
        roles: Set<MemberRole>,
    ) : this(
        id,
        oAuth2Provider,
        roles.map { SimpleGrantedAuthority("ROLE_$it") },
    )
}
