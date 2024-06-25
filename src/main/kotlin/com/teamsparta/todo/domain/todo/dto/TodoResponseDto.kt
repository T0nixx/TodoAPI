package com.teamsparta.todo.domain.todo.dto

import com.teamsparta.todo.domain.todo.model.Todo

data class TodoResponseDto(
    val id: Long,
    val title: String,
    val memberId: Long,
    val content: String,
    val createdAt: String,
    val status: String,
) {
    companion object {
        fun from(todo: Todo): TodoResponseDto {
            return TodoResponseDto(
                id = todo.id!!,
                title = todo.title,
                memberId = todo.member.id!!,
                content = todo.content,
                createdAt = todo.createdAt.toString(),
                status = todo.status.name,
            )
        }
    }
}
