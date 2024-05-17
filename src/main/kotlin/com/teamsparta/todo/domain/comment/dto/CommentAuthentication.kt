package com.teamsparta.todo.domain.comment.dto

data class CommentAuthentication(
    val writer: String,
    val password: String,
)
