package com.teamsparta.todo.domain.comment.dto

data class UpdateCommentRequestDto(
    val auth: AuthenticateCommentDto,
    val data: UpdateCommentDto,
)
