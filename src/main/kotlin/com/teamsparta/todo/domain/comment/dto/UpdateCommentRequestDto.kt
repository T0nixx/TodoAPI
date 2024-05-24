package com.teamsparta.todo.domain.comment.dto

import jakarta.validation.constraints.NotBlank

data class UpdateCommentRequestDto(
    @field:NotBlank
    val content: String,
)
