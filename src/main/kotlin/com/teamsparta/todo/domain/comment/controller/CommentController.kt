package com.teamsparta.todo.domain.comment.controller

import com.teamsparta.todo.domain.comment.dto.AddCommentRequest
import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.todo.service.TodoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/todos/{todoId}/comments")
@RestController
class CommentController(private val todoService: TodoService) {
    @PostMapping("/")
    fun addComment(
        @PathVariable("todoId")
        todoId: Long,
        @RequestBody
        addCommentRequest: AddCommentRequest,
    ): CommentResponse {
        return todoService.addComment(todoId, addCommentRequest)
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable
        todoId: Long,
        @PathVariable
        commentId: Long,
        @RequestBody
        updateCommentRequest: UpdateCommentRequest,
    ): CommentResponse {
        return todoService.updateComment(
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
        deleteCommentRequest: DeleteCommentRequest,
    ) {
        return todoService.deleteComment(
            todoId,
            commentId,
            deleteCommentRequest,
        )
    }

}
