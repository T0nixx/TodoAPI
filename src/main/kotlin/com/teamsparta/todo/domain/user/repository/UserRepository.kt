package com.teamsparta.todo.domain.user.repository

import com.teamsparta.todo.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmailOrUsername(email: String, username: String): Boolean
    fun findByEmail(email: String): User?
    fun existsByUsername(username: String): Boolean
}
