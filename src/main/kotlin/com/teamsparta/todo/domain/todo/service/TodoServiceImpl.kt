package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequest
import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.model.toResponse
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.security.PasswordEncoder
import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequest
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.model.TodoStatus
import com.teamsparta.todo.domain.todo.model.toResponse
import com.teamsparta.todo.domain.todo.model.toWithCommentsResponse
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val passwordEncoder: PasswordEncoder,
) : TodoService {
    override fun getTodoList(): List<TodoResponse> {
        return todoRepository
            .findAll()
            .sortedByDescending { it.createdAt }
            .map { it.toResponse() }
    }

    override fun getTodoById(todoId: Long): TodoWithCommentsResponse {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        val comments = commentRepository.findAllByTodoId(todoId)
        val commentResponses = comments.map { it.toResponse() }
        return todo.toWithCommentsResponse(commentResponses)
    }

    override fun createTodo(createTodoRequest: CreateTodoRequest): TodoResponse {
        val (title, writer, content) = createTodoRequest

        val todo = Todo(title = title, writer = writer, content = content)
        return todoRepository.save(todo).toResponse()
    }

    override fun updateTodo(
        todoId: Long,
        updateTodoRequest: UpdateTodoRequest,
    ): TodoResponse {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)

        val (title, writer, content) = updateTodoRequest
        todo.update(title, writer, content)

        return todoRepository.save(todo).toResponse()
    }

    override fun updateTodoStatus(
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequest,
    ): TodoResponse {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        val (status) = updateTodoStatusRequest

        when (status) {
            TodoStatus.TODO.name -> {
                if (todo.isTodo()) {
                    throw IllegalArgumentException("New status: $status is same with old one.")
                }
                todo.reopen()
            }

            TodoStatus.DONE.name -> {
                if (todo.isDone()) {
                    throw IllegalArgumentException("New status: $status is same with old one.")
                }
                todo.complete()
            }

            else -> throw IllegalArgumentException("$status is invalid status.")
        }

        return todoRepository.save(todo).toResponse()
    }

    override fun deleteTodo(todoId: Long) {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        val comments = commentRepository.findAllByTodoId(todoId)
        comments.forEach { commentRepository.delete(it) }
        todoRepository.delete(todo)
    }

    override fun addComment(
        todoId: Long,
        addCommentRequest: AddCommentRequest,
    ): CommentResponse {
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

    override fun updateComment(
        todoId: Long,
        commentId: Long,
        updateCommentRequest: UpdateCommentRequest,
    ): CommentResponse {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)

        val comment =
            commentRepository.findByIdOrNull(commentId)
                ?: throw ModelNotFoundException("Comment", commentId)
        if (comment.todo.id != todo.id) {
            throw IllegalArgumentException("This Comment (id: $commentId) does not belong to Todo (id: $todoId)")
        }

        val (writer, password, content) = updateCommentRequest

        if (writer != comment.writer || passwordEncoder.matches(
                password,
                comment.salt,
                comment.password,
            ) == false
        ) throw IllegalArgumentException(
            "Writer or Password do not match",
        )

        comment.updateContent(content)
        return commentRepository.save(comment).toResponse()
    }

    override fun deleteComment(
        todoId: Long,
        commentId: Long,
        deleteCommentRequest: DeleteCommentRequest,
    ) {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        val comment =
            commentRepository.findByIdOrNull(commentId)
                ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.todo.id != todo.id) {
            throw IllegalArgumentException("This Comment (id: $commentId) does not belong to Todo (id: $todoId)")
        }

        val (writer, password) = deleteCommentRequest

        if (writer != comment.writer || passwordEncoder.matches(
                password,
                comment.salt,
                comment.password,
            ) == false
        ) throw IllegalArgumentException(
            "Writer or Password do not match",
        )

        commentRepository.delete(comment)
    }
}
