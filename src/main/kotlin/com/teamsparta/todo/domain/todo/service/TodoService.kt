package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest

interface TodoService {
    fun getTodoList(): List<TodoResponse>
    fun getTodoById(todoId: Long): TodoResponse
    fun createTodo(createTodoRequest: CreateTodoRequest): TodoResponse
    fun updateTodo(
        todoId: Long,
        updateTodoRequest: UpdateTodoRequest,
    ): TodoResponse

    fun deleteTodo(todoId: Long)
}
