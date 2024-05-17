package com.teamsparta.todo.domain.comment.dto

data class UpdateCommentRequest(
    val auth: CommentAuthentication,
    val data: UpdateCommentData,
)
