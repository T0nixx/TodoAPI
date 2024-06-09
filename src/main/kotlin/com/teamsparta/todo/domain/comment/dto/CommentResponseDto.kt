package com.teamsparta.todo.domain.comment.dto

data class CommentResponseDto(
    val id: Long,
    val memberId: Long?,
    val socialMemberId: Long?,
    val content: String,
)
