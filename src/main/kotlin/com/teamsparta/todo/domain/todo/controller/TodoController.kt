package com.teamsparta.todo.domain.todo.controller

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest
import com.teamsparta.todo.domain.todo.service.TodoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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
    fun getTodos(): List<TodoResponse> {
        return todoService.getTodoList()
    }

    @GetMapping("/{todoId}")
    fun getTodoById(@PathVariable("todoId") id: Long): TodoResponse {
        return todoService.getTodoById(id)
    }

    @PostMapping
    fun createTodo(
        @RequestBody
        createTodoRequest: CreateTodoRequest,
    ): TodoResponse {
        return todoService.createTodo(createTodoRequest)
    }

    @PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable("todoId") id: Long,
        @RequestBody updateTodoRequest: UpdateTodoRequest,
    ): TodoResponse {
        return todoService.updateTodo(id, updateTodoRequest)
    }

    @DeleteMapping("/{todoId}")
    fun deleteTodo(@PathVariable("todoId") id: Long) {
        todoService.deleteTodo(id)
    }
}
