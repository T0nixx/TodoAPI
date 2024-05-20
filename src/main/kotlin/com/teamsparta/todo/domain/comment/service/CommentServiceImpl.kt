package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.model.toResponse
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.security.PasswordEncoder
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CommentServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val passwordEncoder: PasswordEncoder,
) : CommentService {

    @Transactional
    override fun addComment(
        todoId: Long,
        addCommentRequest: AddCommentRequestDto,
    ): CommentResponseDto {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        val (writer, password, content) = addCommentRequest

        val salt = UUID.randomUUID().toString()
        val comment = Comment(
            writer = writer,
            password = passwordEncoder.encode(password, salt),
            todo = todo,
            content = content,
            salt = salt,
        )

        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    override fun updateComment(
        todoId: Long,
        commentId: Long,
        updateCommentRequest: UpdateCommentRequestDto,
    ): CommentResponseDto {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)

        val comment =
            commentRepository.findByIdOrNull(commentId)
                ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.todo.id != todo.id) {
            throw IllegalArgumentException("This Comment (id: $commentId) does not belong to Todo (id: $todoId).")
        }

        val (auth, data) = updateCommentRequest

        if (auth.writer != comment.writer || passwordEncoder.matches(
                auth.password,
                comment.salt,
                comment.password,
            ) == false
        ) throw IllegalArgumentException(
            "Writer or Password do not match",
        )

        comment.updateContent(data.content)
        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    override fun deleteComment(
        todoId: Long,
        commentId: Long,
        deleteCommentRequest: DeleteCommentRequestDto,
    ) {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        val comment =
            commentRepository.findByIdOrNull(commentId)
                ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.todo.id != todo.id) {
            throw IllegalArgumentException("This Comment (id: $commentId) does not belong to Todo (id: $todoId).")
        }

        val (auth) = deleteCommentRequest

        if (auth.writer != comment.writer || passwordEncoder.matches(
                auth.password,
                comment.salt,
                comment.password,
            ) == false
        ) throw IllegalArgumentException(
            "Writer or Password do not match.",
        )

        commentRepository.delete(comment)
    }
}
