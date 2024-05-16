package com.teamsparta.todo.domain.comment.dto

data class AddCommentRequest(
    val writer: String,
    val password: String,
    val content: String,
)
