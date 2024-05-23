package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import org.springframework.data.domain.Sort
import org.springframework.security.core.userdetails.User

interface TodoService {
    fun getTodoList(
        sortDirection: Sort.Direction,
        writer: String?,
        cursor: Long,
    ): List<TodoResponseDto>

    fun getTodoById(todoId: Long): TodoWithCommentsResponseDto

    fun createTodo(
        user: User,
        createTodoRequest: CreateTodoRequestDto,
    ): TodoResponseDto

    fun updateTodo(
        user: User,
        todoId: Long,
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto

    fun updateTodoStatus(
        user: User,
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto

    fun deleteTodo(user: User, todoId: Long)

}
