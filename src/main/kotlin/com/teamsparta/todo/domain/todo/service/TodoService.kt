package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequest
import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequest

interface TodoService {
    fun getTodoList(): List<TodoResponse>

    fun getTodoById(todoId: Long): TodoWithCommentsResponse

    fun createTodo(createTodoRequest: CreateTodoRequest): TodoResponse

    fun updateTodo(
        todoId: Long,
        updateTodoRequest: UpdateTodoRequest,
    ): TodoResponse

    fun updateTodoStatus(
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequest,
    ): TodoResponse

    fun deleteTodo(todoId: Long)

    fun addComment(
        todoId: Long,
        addCommentRequest: AddCommentRequest,
    ): CommentResponse

    fun updateComment(
        todoId: Long,
        commentId: Long,
        updateCommentRequest: UpdateCommentRequest,
    ): CommentResponse

    fun deleteComment(
        todoId: Long,
        commentId: Long,
        deleteCommentRequest: DeleteCommentRequest,
    )

}
