package com.teamsparta.todo.domain.todo.dto

data class TodoResponse(
    val id: Long,
    val title: String,
    val writer: String,
    val content: String,
    val createdAt: String,
)
