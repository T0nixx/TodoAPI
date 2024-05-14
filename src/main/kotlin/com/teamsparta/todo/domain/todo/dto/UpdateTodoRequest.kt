package com.teamsparta.todo.domain.todo.dto

data class UpdateTodoRequest(
    val title: String,
    val writer: String,
    val content: String,
)
