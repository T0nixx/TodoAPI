package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto

interface CommentService {
    fun addComment(
        todoId: Long,
        addCommentRequest: AddCommentRequestDto,
    ): CommentResponseDto

    fun updateComment(
        todoId: Long,
        commentId: Long,
        updateCommentRequest: UpdateCommentRequestDto,
    ): CommentResponseDto

    fun deleteComment(
        todoId: Long,
        commentId: Long,
        deleteCommentRequest: DeleteCommentRequestDto,
    )
}
