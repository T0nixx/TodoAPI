package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.model.toResponseDto
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import com.teamsparta.todo.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) : CommentService {

    @Transactional
    override fun addComment(
        user: User,
        todoId: Long,
        addCommentRequest: AddCommentRequestDto,
    ): CommentResponseDto {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        if (userRepository.existsByUsername(user.username) == false) throw IllegalStateException(
            "Username: ${user.username} does not exists",
        )

        val (content) = addCommentRequest

        val comment = Comment(
            writer = user.username,
            todo = todo,
            content = content,
        )

        return commentRepository.save(comment).toResponseDto()
    }

    @Transactional
    override fun updateComment(
        user: User,
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

        val (content) = updateCommentRequest

        if (userRepository.existsByUsername(user.username) == false) throw IllegalStateException(
            "Username: ${user.username} does not exists",
        )

        if (user.username != comment.writer) throw IllegalStateException("User: ${user.username} is not writer of comment (id: $commentId).")


        comment.updateContent(content)
        return commentRepository.save(comment).toResponseDto()
    }

    @Transactional
    override fun deleteComment(
        user: User,
        todoId: Long,
        commentId: Long,
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

        if (userRepository.existsByUsername(user.username) == false) throw IllegalStateException(
            "Username: ${user.username} does not exists",
        )

        if (user.username != comment.writer) throw IllegalStateException("User: ${user.username} is not writer of comment (id: $commentId).")

        commentRepository.delete(comment)
    }
}
