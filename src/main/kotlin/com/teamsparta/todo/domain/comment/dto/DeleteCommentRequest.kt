package com.teamsparta.todo.domain.comment.dto

data class DeleteCommentRequest(
    val writer: String,
    val password: String,
)
