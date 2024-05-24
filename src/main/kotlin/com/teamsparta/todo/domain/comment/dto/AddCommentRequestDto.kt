package com.teamsparta.todo.domain.comment.dto

import jakarta.validation.constraints.NotBlank

data class AddCommentRequestDto(
    @field:NotBlank
    val content: String,
)
