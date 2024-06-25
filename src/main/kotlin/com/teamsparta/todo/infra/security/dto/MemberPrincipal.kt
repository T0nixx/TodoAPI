package com.teamsparta.todo.infra.security.dto

import com.teamsparta.todo.domain.member.model.MemberRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class MemberPrincipal(
    val id: Long,
    val isSocial: Boolean,
    val authorities: Collection<GrantedAuthority>,
) {
    constructor(
        id: Long,
        isSocial: Boolean,
        roles: Set<MemberRole>,
    ) : this(
        id,
        isSocial = isSocial,
        roles.map { SimpleGrantedAuthority("ROLE_$it") },
    )
}
