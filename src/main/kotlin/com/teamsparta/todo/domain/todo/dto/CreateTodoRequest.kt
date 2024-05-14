package com.teamsparta.todo.domain.todo.dto

data class CreateTodoRequest(
    val title: String,
    val writer: String,
    val content: String,
)
