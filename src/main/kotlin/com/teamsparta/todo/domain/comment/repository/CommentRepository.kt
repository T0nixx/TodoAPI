package com.teamsparta.todo.domain.comment.repository

import com.teamsparta.todo.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByTodoId(todoId: Long): List<Comment>
    fun findByTodoIdIn(todoIds: List<Long>): List<Comment>
}
