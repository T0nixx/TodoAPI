package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todo.infra.security.dto.MemberPrincipal

interface CommentService {
    fun addComment(
        principal: MemberPrincipal,
        todoId: Long,
        addCommentRequest: AddCommentRequestDto,
    ): CommentResponseDto

    fun updateComment(
        principal: MemberPrincipal,
        todoId: Long,
        commentId: Long,
        updateCommentRequest: UpdateCommentRequestDto,
    ): CommentResponseDto

    fun deleteComment(
        principal: MemberPrincipal,
        todoId: Long,
        commentId: Long,
    )
}
