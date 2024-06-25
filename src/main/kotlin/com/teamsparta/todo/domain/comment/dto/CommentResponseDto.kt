package com.teamsparta.todo.domain.comment.dto

import com.teamsparta.todo.domain.comment.model.Comment

data class CommentResponseDto(
    val id: Long,
    val memberId: Long?,
    val content: String,
) {
    companion object {
        fun from(comment: Comment): CommentResponseDto {
            return CommentResponseDto(
                id = comment.id!!,
                memberId = comment.member?.id,
                content = comment.content,
            )
        }
    }
}
