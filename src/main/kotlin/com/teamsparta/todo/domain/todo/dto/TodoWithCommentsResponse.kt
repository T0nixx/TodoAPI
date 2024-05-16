package com.teamsparta.todo.domain.todo.dto

import com.teamsparta.todo.domain.comment.dto.CommentResponse

data class TodoWithCommentsResponse(
    val id: Long,
    val title: String,
    val writer: String,
    val content: String,
    val createdAt: String,
    val status: String,
    val comments: List<CommentResponse>,
)
