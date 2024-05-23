package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto
import org.springframework.security.core.userdetails.User

interface CommentService {
    fun addComment(
        user: User,
        todoId: Long,
        addCommentRequest: AddCommentRequestDto,
    ): CommentResponseDto

    fun updateComment(
        user: User,
        todoId: Long,
        commentId: Long,
        updateCommentRequest: UpdateCommentRequestDto,
    ): CommentResponseDto

    fun deleteComment(
        user: User,
        todoId: Long,
        commentId: Long,
    )
}
