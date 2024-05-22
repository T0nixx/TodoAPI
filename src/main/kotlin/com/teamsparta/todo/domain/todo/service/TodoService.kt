package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import com.teamsparta.todo.domain.todo.model.SortDirection

interface TodoService {
    fun getTodoList(
        sortDirection: SortDirection,
        writer: String?,
    ): List<TodoResponseDto>

    fun getTodoById(todoId: Long): TodoWithCommentsResponseDto

    fun createTodo(createTodoRequest: CreateTodoRequestDto): TodoResponseDto

    fun updateTodo(
        todoId: Long,
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto

    fun updateTodoStatus(
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto

    fun deleteTodo(todoId: Long)

}
