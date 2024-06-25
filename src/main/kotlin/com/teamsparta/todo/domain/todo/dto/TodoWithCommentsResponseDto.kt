package com.teamsparta.todo.domain.todo.dto

import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.todo.model.Todo

data class TodoWithCommentsResponseDto(
    val id: Long,
    val title: String,
    val memberId: Long,
    val content: String,
    val createdAt: String,
    val status: String,
    val comments: List<CommentResponseDto>,
) {
    companion object {
        fun from(
            todo: Todo,
            comments: List<Comment>,
        ): TodoWithCommentsResponseDto {
            return TodoWithCommentsResponseDto(
                id = todo.id!!,
                title = todo.title,
                memberId = todo.member.id!!,
                content = todo.content,
                createdAt = todo.createdAt.toString(),
                status = todo.status.name,
                comments = comments.map { CommentResponseDto.from(it) },
            )
        }
    }
}
