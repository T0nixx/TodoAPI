package com.teamsparta.todo.domain.comment.dto

data class CommentResponseDto(
    val id: Long,
    val writer: String,
    val content: String,
)
