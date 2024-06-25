package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.member.repository.MemberRepository
import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
) : TodoService {
    override fun getTodoList(
        sortDirection: Sort.Direction,
        memberId: Long?,
        cursor: Long?,
    ): List<TodoWithCommentsResponseDto> {
        val todos =
            todoRepository.findPageFromCursorByWriterId(
                cursor = cursor,
                sortDirection = sortDirection,
                memberId = memberId,
            )
        val comments =
            commentRepository.findByTodoIdIn(todos.map { it.id!! })

        return todos.map { TodoWithCommentsResponseDto.from(it, comments) }
    }

    override fun getTodoById(todoId: Long): TodoWithCommentsResponseDto {
        val todo = getTodoOrThrow(todoId)
        val comments = commentRepository.findAllByTodoId(todoId)

        return TodoWithCommentsResponseDto.from(todo, comments)
    }

    @Transactional
    override fun createTodo(
        memberId: Long,
        createTodoRequest: CreateTodoRequestDto,
    ): TodoResponseDto {
        val (title, content) = createTodoRequest

        return memberRepository.findByIdOrNull(memberId)?.let { member ->
            Todo(
                title = title,
                content = content,
                member = member,
            )
                .let { todoRepository.save(it) }
                .let { TodoResponseDto.from(it) }
        } ?: throw ModelNotFoundException("Member", memberId)
    }

    @Transactional
    override fun updateTodo(
        memberId: Long,
        todoId: Long,
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto {
        val todo = getTodoOrThrow(todoId)

        checkMemberIsTodoWriter(memberId, todo)

        val (title, content) = updateTodoRequest
        todo.update(title, content)

        return TodoResponseDto.from(todo)
    }

    @Transactional
    override fun updateTodoStatus(
        memberId: Long,
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto {
        val todo = getTodoOrThrow(todoId)

        checkMemberIsTodoWriter(memberId, todo)

        val (status) = updateTodoStatusRequest

        todo.updateStatus(status)

        return TodoResponseDto.from(todo)
    }

    @Transactional
    override fun deleteTodo(
        memberId: Long,
        todoId: Long,
    ) {
        val todo = getTodoOrThrow(todoId)
        checkMemberIsTodoWriter(memberId, todo)
        val comments = commentRepository.findAllByTodoId(todoId)
        commentRepository.deleteAll(comments)
        todoRepository.delete(todo)
    }

    private fun checkMemberIsTodoWriter(
        memberId: Long,
        todo: Todo,
    ) {
        if (memberId != todo.member.id!!) {
            throw IllegalStateException(
                "Member $memberId is not the writer of Todo ${todo.id}.",
            )
        }
    }

    private fun getTodoOrThrow(todoId: Long): Todo {
        return todoRepository.findByIdOrNull(todoId)
            ?: throw ModelNotFoundException("Todo", todoId)
    }
}
