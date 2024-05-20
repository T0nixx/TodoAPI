package com.teamsparta.todo.domain.todo.dto

data class UpdateTodoRequestDto(
    val title: String,
    val writer: String,
    val content: String,
)
