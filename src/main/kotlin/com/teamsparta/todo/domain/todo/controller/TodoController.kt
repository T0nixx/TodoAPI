package com.teamsparta.todo.domain.todo.controller

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import com.teamsparta.todo.domain.todo.service.TodoService
import com.teamsparta.todo.infra.security.dto.MemberPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @GetMapping
    fun getTodos(
        @RequestParam
        sortDirection: Sort.Direction?,
        @RequestParam
        memberId: Long?,
        @RequestParam
        socialMemberId: Long?,
        @RequestParam
        cursor: Long?,
    ): ResponseEntity<List<TodoWithCommentsResponseDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(
            todoService.getTodoList(
                sortDirection = sortDirection ?: Sort.Direction.DESC,
                memberId = memberId,
                socialMemberId = socialMemberId,
                cursor = cursor,
            ),
        )
    }

    @Operation(
        summary = "특정 TODO 조회",
        description = "선택된 TODO와 TODO에 달린 Comment들을 조회합니다.",
    )
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @GetMapping("/{todoId}")
    fun getTodoById(
        @PathVariable("todoId")
        id: Long,
    ): ResponseEntity<TodoWithCommentsResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(todoService.getTodoById(id))
    }

    @Operation(
        summary = "TODO 생성",
    )
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400")
    @PostMapping
    fun createTodo(
        @AuthenticationPrincipal
        principal: MemberPrincipal,
        @Valid
        @RequestBody
        createTodoRequest: CreateTodoRequestDto,
    ): ResponseEntity<TodoResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(todoService.createTodo(principal, createTodoRequest))
    }

    @Operation(
        summary = "TODO 수정",
    )
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @PutMapping("/{todoId}")
    fun updateTodo(
        @AuthenticationPrincipal
        principal: MemberPrincipal,
        @PathVariable("todoId")
        id: Long,
        @Valid
        @RequestBody
        updateTodoRequest: UpdateTodoRequestDto,
    ): ResponseEntity<TodoResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(todoService.updateTodo(principal, id, updateTodoRequest))
    }

    @Operation(
        summary = "TODO 삭제",
    )
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "400")
    @DeleteMapping("/{todoId}")
    fun deleteTodo(
        @AuthenticationPrincipal
        principal: MemberPrincipal,
        @PathVariable("todoId")
        id: Long,
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(todoService.deleteTodo(principal, id))
    }

    @Operation(
        summary = "TODO 상태 변경",
        description = "TODO의 상태를 변경합니다. 가능한 상태: TODO, DONE",
    )
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @PatchMapping("/{todoId}/status")
    fun updateTodoStatus(
        @AuthenticationPrincipal
        principal: MemberPrincipal,
        @PathVariable("todoId")
        id: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): ResponseEntity<TodoResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                todoService.updateTodoStatus(
                    principal,
                    id,
                    updateTodoStatusRequest,
                ),
            )
    }
}
