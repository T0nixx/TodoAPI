package com.teamsparta.todo.domain.todo.dto

data class TodoResponseDto(
    val id: Long,
    val title: String,
    val memberId: Long?,
    val socialMemberId: Long?,
    val content: String,
    val createdAt: String,
    val status: String,
)
