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
        memberId: Long?,
        cursor: Long?,
    ): List<TodoWithCommentsResponseDto>

    fun getTodoById(todoId: Long): TodoWithCommentsResponseDto

    fun createTodo(
        memberId: Long,
        createTodoRequest: CreateTodoRequestDto,
    ): TodoResponseDto

    fun updateTodo(
        memberId: Long,
        todoId: Long,
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto

    fun updateTodoStatus(
        memberId: Long,
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto

    fun deleteTodo(
        memberId: Long,
        todoId: Long,
    )
}
