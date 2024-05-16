package com.teamsparta.todo.domain.comment.dto

data class UpdateCommentRequest(
    val writer: String,
    val password: String,
    val content: String,
)
