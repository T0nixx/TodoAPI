package com.teamsparta.todo.domain.comment.controller

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todo.domain.comment.service.CommentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/todos/{todoId}/comments")
@RestController
class CommentController(private val commentService: CommentService) {
    @PostMapping("/")
    fun addComment(
        @PathVariable("todoId")
        todoId: Long,
        @RequestBody
        addCommentRequest: AddCommentRequestDto,
    ): CommentResponseDto {
        return commentService.addComment(todoId, addCommentRequest)
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable
        todoId: Long,
        @PathVariable
        commentId: Long,
        @RequestBody
        updateCommentRequest: UpdateCommentRequestDto,
    ): CommentResponseDto {
        return commentService.updateComment(
            todoId,
            commentId,
            updateCommentRequest,
        )
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable
        todoId: Long,
        @PathVariable
        commentId: Long,
        @RequestBody
        deleteCommentRequest: DeleteCommentRequestDto,
    ) {
        return commentService.deleteComment(
            todoId,
            commentId,
            deleteCommentRequest,
        )
    }

}
