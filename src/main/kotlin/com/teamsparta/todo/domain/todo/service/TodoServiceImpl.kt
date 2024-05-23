package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.model.toResponseDto
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.model.toResponseDto
import com.teamsparta.todo.domain.todo.model.toWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import com.teamsparta.todo.domain.user.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) : TodoService {

    override fun getTodoList(
        sortDirection: Sort.Direction,
        writer: String?,
        cursor: Long,
    ): List<TodoResponseDto> {
        val pageable = PageRequest.of(0, 10, sortDirection, "createdAt")

        val todos = when {
            cursor == 0L -> {
                if (writer == null) todoRepository.findAll(pageable)
                else {
                    todoRepository.findAllByWriter(
                        writer,
                        pageable,
                    )
                }
            }

            sortDirection == Sort.Direction.DESC -> {
                if (writer == null) todoRepository.findNextTodoPage(
                    cursor,
                    pageable,
                )
                else todoRepository.findNextTodoPageByWriter(
                    cursor,
                    writer,
                    pageable,
                )
            }

            else -> { // ASC
                if (writer == null) todoRepository.findPreviousTodoPage(
                    cursor,
                    pageable,
                )
                else todoRepository.findPreviousTodoPageByWriter(
                    cursor,
                    writer,
                    pageable,
                )
            }
        }
        return todos.map { it.toResponseDto() }
    }

    override fun getTodoById(todoId: Long): TodoWithCommentsResponseDto {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        val comments = commentRepository.findAllByTodoId(todoId)
        val commentResponses = comments.map { it.toResponseDto() }
        return todo.toWithCommentsResponseDto(commentResponses)
    }

    @Transactional
    override fun createTodo(
        user: User,
        createTodoRequest: CreateTodoRequestDto,
    ): TodoResponseDto {
        val (title, content) = createTodoRequest
        val writer = user.username

        if (userRepository.existsByUsername(writer) == false) throw IllegalArgumentException(
            "Username: $writer does not exist.",
        )
        val todo =
            Todo(title = title, writer = writer, content = content)
        return todoRepository.save(todo).toResponseDto()
    }

    @Transactional
    override fun updateTodo(
        user: User,
        todoId: Long,
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        if (todo.writer != user.username) throw IllegalStateException(
            "Username: ${user.username} is not the writer of todo (id: ${todoId}).",
        )

        val (title, content) = updateTodoRequest
        todo.update(title, content)

        return todoRepository.save(todo).toResponseDto()
    }

    @Transactional
    override fun updateTodoStatus(
        user: User,
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)

        if (todo.writer != user.username) throw IllegalStateException(
            "Username: ${user.username} is not the writer of the todo: ${todoId}.",
        )

        val (status) = updateTodoStatusRequest

        todo.updateStatus(status)

        return todoRepository.save(todo).toResponseDto()
    }

    @Transactional
    override fun deleteTodo(user: User, todoId: Long) {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        if (todo.writer != user.username) throw IllegalStateException(
            "Username: ${user.username} is not the writer of the todo: ${todoId}.",
        )

        val comments = commentRepository.findAllByTodoId(todoId)
        comments.forEach { commentRepository.delete(it) }
        todoRepository.delete(todo)
    }
}
