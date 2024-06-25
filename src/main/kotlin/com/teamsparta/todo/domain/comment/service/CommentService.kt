package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto

interface CommentService {
    fun addComment(
        memberId: Long,
        todoId: Long,
        addCommentRequest: AddCommentRequestDto,
    ): CommentResponseDto

    fun updateComment(
        memberId: Long,
        todoId: Long,
        commentId: Long,
        updateCommentRequest: UpdateCommentRequestDto,
    ): CommentResponseDto

    fun deleteComment(
        memberId: Long,
        todoId: Long,
        commentId: Long,
    )
}
