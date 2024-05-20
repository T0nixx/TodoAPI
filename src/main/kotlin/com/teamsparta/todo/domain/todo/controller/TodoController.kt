package com.teamsparta.todo.domain.todo.controller

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import com.teamsparta.todo.domain.todo.service.TodoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/todos")
@RestController
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun getTodos(): List<TodoResponseDto> {
        return todoService.getTodoList()
    }

    @GetMapping("/{todoId}")
    fun getTodoById(
        @PathVariable("todoId")
        id: Long,
    ): TodoWithCommentsResponseDto {
        return todoService.getTodoById(id)
    }

    @PostMapping
    fun createTodo(
        @RequestBody
        createTodoRequest: CreateTodoRequestDto,
    ): TodoResponseDto {
        return todoService.createTodo(createTodoRequest)
    }

    @PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable("todoId")
        id: Long,
        @RequestBody
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto {
        return todoService.updateTodo(id, updateTodoRequest)
    }

    @DeleteMapping("/{todoId}")
    fun deleteTodo(
        @PathVariable("todoId")
        id: Long,
    ) {
        todoService.deleteTodo(id)
    }

    @PatchMapping("/{todoId}")
    fun updateTodoStatus(
        @PathVariable("todoId")
        id: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto {
        return todoService.updateTodoStatus(id, updateTodoStatusRequest)
    }
}
