package com.teamsparta.todo.domain.comment.dto

data class CommentResponse(
    val id: Long,
    val writer: String,
    val content: String,
)
