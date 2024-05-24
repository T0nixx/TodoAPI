package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.model.toResponseDto
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.todo.model.Todo
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
        val todo = getTodoOrThrow(todoId)

        val writerId = user.username.toLong()
        val writer = getWriterOrThrow(writerId)

        val (content) = addCommentRequest

        val comment = Comment(
            writer = writer,
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
        val todo = getTodoOrThrow(todoId)

        val comment = getCommentOrThrow(commentId)

        assertCommentBelongsToTodo(comment, todo)
        assertUserIsCommentWriter(user, comment)
        val (content) = updateCommentRequest

        comment.updateContent(content)
        return commentRepository.save(comment).toResponseDto()
    }

    @Transactional
    override fun deleteComment(
        user: User,
        todoId: Long,
        commentId: Long,
    ) {
        val todo = getTodoOrThrow(todoId)
        val comment = getCommentOrThrow(commentId)

        assertCommentBelongsToTodo(comment, todo)
        assertUserIsCommentWriter(user, comment)

        commentRepository.delete(comment)
    }

    private fun assertCommentBelongsToTodo(comment: Comment, todo: Todo) {
        if (comment.todo.id != todo.id) {
            throw IllegalArgumentException("This Comment (id: $comment.id) does not belong to Todo (id: $todo.id).")
        }
    }

    private fun assertUserIsCommentWriter(user: User, comment: Comment) {
        val appUserId = user.username.toLong()
        val appUser = getWriterOrThrow(appUserId)

        if (appUser != comment.writer) throw IllegalStateException("User: $appUserId is not writer of comment (id: $comment.id).")
    }

    private fun getTodoOrThrow(todoId: Long): Todo {
        return todoRepository.findByIdOrNull(todoId)
            ?: throw ModelNotFoundException("Todo", todoId)
    }

    private fun getCommentOrThrow(commentId: Long): Comment {
        return commentRepository.findByIdOrNull(commentId)
            ?: throw ModelNotFoundException("Comment", commentId)
    }

    private fun getWriterOrThrow(writerId: Long) =
        userRepository.findByIdOrNull(writerId)
            ?: throw IllegalStateException(
                "User: $writerId does not exists",
            )

}
