package com.teamsparta.todo.domain.todo.dto

import jakarta.validation.constraints.Size

data class UpdateTodoRequestDto(
    @field:Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하여야 합니다.")
    val title: String,
    val writer: String,
    @field:Size(min = 1, max = 1000, message = "내용은 1자 이상 1000자 이하여야 합니다.")
    val content: String,
)
