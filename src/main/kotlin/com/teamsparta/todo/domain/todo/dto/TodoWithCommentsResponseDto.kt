package com.teamsparta.todo.domain.todo.dto

import com.teamsparta.todo.domain.comment.dto.CommentResponseDto

data class TodoWithCommentsResponseDto(
    val id: Long,
    val title: String,
    val memberId: Long?,
    val socialMemberId: Long?,
    val content: String,
    val createdAt: String,
    val status: String,
    val comments: List<CommentResponseDto>,
)
