package com.teamsparta.todo.domain.comment.dto

data class AuthenticateCommentDto(
    val writer: String,
    val password: String,
)
