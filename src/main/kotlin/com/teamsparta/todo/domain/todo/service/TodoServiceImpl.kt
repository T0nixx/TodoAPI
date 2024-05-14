package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.model.toResponse
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TodoServiceImpl(private val todoRepository: TodoRepository) : TodoService {
    override fun getTodoList(): List<TodoResponse> {
        return todoRepository
            .findAll()
            .sortedBy { it.createdAt }
            .map { it.toResponse() }
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)
        return todo.toResponse()
    }

    override fun createTodo(createTodoRequest: CreateTodoRequest): TodoResponse {
        val (title, writer, content) = createTodoRequest

        val todo = Todo(title = title, writer = writer, content = content)
        return todoRepository
            .save(todo)
            .toResponse()
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

    override fun deleteTodo(todoId: Long) {
        val todo =
            todoRepository.findByIdOrNull(todoId)
                ?: throw ModelNotFoundException("Todo", todoId)

        todoRepository.delete(todo)
    }
}
