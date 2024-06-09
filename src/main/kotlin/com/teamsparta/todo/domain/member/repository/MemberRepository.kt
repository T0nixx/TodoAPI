package com.teamsparta.todo.domain.member.repository

import com.teamsparta.todo.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByEmailOrUsername(email: String, username: String): Boolean
    fun findByEmail(email: String): Member?
    fun existsByUsername(username: String): Boolean
}
