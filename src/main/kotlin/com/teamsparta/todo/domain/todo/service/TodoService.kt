package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import com.teamsparta.todo.infra.security.dto.MemberPrincipal
import org.springframework.data.domain.Sort

interface TodoService {
    fun getTodoList(
        sortDirection: Sort.Direction,
        memberId: Long?,
        socialMemberId: Long?,
        cursor: Long?,
    ): List<TodoResponseDto>

    fun getTodoById(todoId: Long): TodoWithCommentsResponseDto

    fun createTodo(
        principal: MemberPrincipal,
        createTodoRequest: CreateTodoRequestDto,
    ): TodoResponseDto

    fun updateTodo(
        principal: MemberPrincipal,
        todoId: Long,
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto

    fun updateTodoStatus(
        principal: MemberPrincipal,
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto

    fun deleteTodo(principal: MemberPrincipal, todoId: Long)
}
