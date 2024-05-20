package com.teamsparta.todo.domain.todo.controller

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import com.teamsparta.todo.domain.todo.service.TodoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/todos")
@RestController
class TodoController(private val todoService: TodoService) {

    @Operation(
        summary = "TODO 목록 조회",
        description = "TODO 목록을 작성일을 기준으로 정렬하여 조회합니다.",
    )
    @GetMapping
    fun getTodos(
        @RequestParam
        sortDirection: String,
    ): List<TodoResponseDto> {
        return todoService.getTodoList(sortDirection)
    }

    @Operation(
        summary = "특정 TODO 조회",
    )
    @GetMapping("/{todoId}")
    fun getTodoById(
        @PathVariable("todoId")
        id: Long,
    ): TodoWithCommentsResponseDto {
        return todoService.getTodoById(id)
    }

    @Operation(
        summary = "TODO 생성",
    )
    @PostMapping
    fun createTodo(
        @RequestBody
        createTodoRequest: CreateTodoRequestDto,
    ): TodoResponseDto {
        return todoService.createTodo(createTodoRequest)
    }

    @Operation(
        summary = "TODO 수정",
    )
    @PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable("todoId")
        id: Long,
        @RequestBody
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto {
        return todoService.updateTodo(id, updateTodoRequest)
    }

    @Operation(
        summary = "TODO 삭제",
    )
    @DeleteMapping("/{todoId}")
    fun deleteTodo(
        @PathVariable("todoId")
        id: Long,
    ) {
        todoService.deleteTodo(id)
    }

    @Operation(
        summary = "TODO 상태 변경",
        description = "TODO의 상태를 변경합니다. 가능한 상태: TODO, DONE",
    )
    @PatchMapping("/{todoId}")
    fun updateTodoStatus(
        @PathVariable("todoId")
        id: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto {
        return todoService.updateTodoStatus(id, updateTodoStatusRequest)
    }
}
