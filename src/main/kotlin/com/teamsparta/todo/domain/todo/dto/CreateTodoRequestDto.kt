package com.teamsparta.todo.domain.todo.dto

data class CreateTodoRequestDto(
    val title: String,
    val writer: String,
    val content: String,
)
