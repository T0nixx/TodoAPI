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
        writerId: Long?,
        cursor: Long,
    ): List<TodoResponseDto> {
        val todos = when {
            cursor == 0L -> {
                if (writerId == null) todoRepository.findPage(
                    sortDirection,
                )
                else {
                    todoRepository.findPageByWriterId(
                        writerId = writerId,
                        sortDirection = sortDirection,
                    )
                }
            }

            else -> todoRepository.findPageFromCursor(
                cursor = cursor,
                writerId = writerId,
                sortDirection = sortDirection,
            )
        }
        return todos.map { it.toResponseDto() }
    }

    override fun getTodoById(todoId: Long): TodoWithCommentsResponseDto {
        val todo = getTodoOrThrow(todoId)
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
        val writerId = user.username.toLong()
        val writer = getAppUserOrThrow(writerId)
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
        val todo = getTodoOrThrow(todoId)

        assertUserIsTodoWriter(user, todo)

        val (title, content) = updateTodoRequest
        todo.update(title, content)

        return todo.toResponseDto()
    }

    @Transactional
    override fun updateTodoStatus(
        user: User,
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto {
        val todo = getTodoOrThrow(todoId)

        assertUserIsTodoWriter(user, todo)

        val (status) = updateTodoStatusRequest

        todo.updateStatus(status)

        return todo.toResponseDto()
    }

    @Transactional
    override fun deleteTodo(user: User, todoId: Long) {
        val todo = getTodoOrThrow(todoId)
        assertUserIsTodoWriter(user, todo)
        val comments = commentRepository.findAllByTodoId(todoId)
        comments.forEach { commentRepository.delete(it) }
        todoRepository.delete(todo)
    }

    private fun assertUserIsTodoWriter(user: User, todo: Todo) {
        val appUserId = user.username.toLong()
        val appUser = getAppUserOrThrow(appUserId)
        if (todo.writer != appUser) throw IllegalStateException(
            "User: ${user.username} is not the writer of the todo: ${todo.id}.",
        )
    }

    private fun getTodoOrThrow(todoId: Long): Todo {
        return todoRepository.findByIdOrNull(todoId)
            ?: throw ModelNotFoundException("Todo", todoId)
    }

    private fun getAppUserOrThrow(appUserId: Long) =
        userRepository.findByIdOrNull(appUserId)
            ?: throw IllegalStateException(
                "User: $appUserId does not exists",
            )
}

