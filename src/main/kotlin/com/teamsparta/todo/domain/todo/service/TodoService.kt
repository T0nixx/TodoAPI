package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import org.springframework.data.domain.Sort

interface TodoService {
    fun getTodoList(
        sortDirection: Sort.Direction,
        writer: String?,
        cursor: Long,
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
