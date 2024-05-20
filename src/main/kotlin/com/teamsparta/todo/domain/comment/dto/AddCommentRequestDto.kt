package com.teamsparta.todo.domain.comment.dto

data class AddCommentRequestDto(
    val writer: String,
    val password: String,
    val content: String,
)
