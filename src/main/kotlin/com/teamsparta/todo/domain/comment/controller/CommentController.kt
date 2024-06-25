package com.teamsparta.todo.domain.comment.controller

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todo.domain.comment.service.CommentService
import com.teamsparta.todo.infra.security.dto.MemberPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    @Operation(summary = "댓글 생성")
    @PostMapping("/")
    fun addComment(
        @AuthenticationPrincipal
        principal: MemberPrincipal,
        @PathVariable("todoId")
        todoId: Long,
        @Valid
        @RequestBody
        addCommentRequest: AddCommentRequestDto,
    ): ResponseEntity<CommentResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                commentService.addComment(
                    principal.id,
                    todoId,
                    addCommentRequest,
                ),
            )
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    fun updateComment(
        @AuthenticationPrincipal
        principal: MemberPrincipal,
        @PathVariable
        todoId: Long,
        @PathVariable
        commentId: Long,
        @Valid
        @RequestBody
        updateCommentRequest: UpdateCommentRequestDto,
    ): ResponseEntity<CommentResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(
            commentService.updateComment(
                principal.id,
                todoId,
                commentId,
                updateCommentRequest,
            ),
        )
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @AuthenticationPrincipal
        principal: MemberPrincipal,
        @PathVariable
        todoId: Long,
        @PathVariable
        commentId: Long,
    ): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.OK).body(
            commentService.deleteComment(
                principal.id,
                todoId,
                commentId,
            ),
        )
    }
}
