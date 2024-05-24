package com.teamsparta.todo.domain.comment.dto

data class CommentResponseDto(
    val id: Long,
    val writerId: Long,
    val content: String,
)
