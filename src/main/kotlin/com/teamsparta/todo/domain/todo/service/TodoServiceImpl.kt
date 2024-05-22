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
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
) : TodoService {

    override fun getTodoList(): List<TodoResponseDto> {
        return todoRepository
            .findAll()
            .sortedByDescending { it.createdAt }
            .map { it.toResponseDto() }
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
    override fun createTodo(createTodoRequest: CreateTodoRequestDto): TodoResponseDto {
        val (title, writer, content) = createTodoRequest

        val todo = Todo(title = title, writer = writer, content = content)
        return todoRepository.save(todo).toResponseDto()
    }

    @Transactional
    override fun updateTodo(
        todoId: Long,
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)

        val (title, writer, content) = updateTodoRequest
        todo.update(title, writer, content)

        return todoRepository.save(todo).toResponseDto()
    }

    @Transactional
    override fun updateTodoStatus(
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        val (status) = updateTodoStatusRequest

        todo.updateStatus(status)

        return todoRepository.save(todo).toResponseDto()
    }

    @Transactional
    override fun deleteTodo(todoId: Long) {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        val comments = commentRepository.findAllByTodoId(todoId)
        comments.forEach { commentRepository.delete(it) }
        todoRepository.delete(todo)
    }
}
