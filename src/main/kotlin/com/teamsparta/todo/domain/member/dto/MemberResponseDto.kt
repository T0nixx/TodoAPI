package com.teamsparta.todo.domain.member.dto

import com.teamsparta.todo.domain.member.model.Member

data class MemberResponseDto(
    val nickname: String,
    val id: Long,
) {
    companion object {
        fun from(member: Member): MemberResponseDto {
            return MemberResponseDto(
                nickname = member.nickname,
                id = member.id!!,
            )
        }
    }
}
