package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.model.toResponseDto
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.member.repository.MemberRepository
import com.teamsparta.todo.domain.socialmember.repository.SocialMemberRepository
import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.UpdateTodoStatusRequestDto
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.model.toResponseDto
import com.teamsparta.todo.domain.todo.model.toWithCommentsResponseDto
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import com.teamsparta.todo.infra.security.dto.MemberPrincipal
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val socialMemberRepository: SocialMemberRepository,
) : TodoService {
    override fun getTodoList(
        sortDirection: Sort.Direction,
        memberId: Long?,
        socialMemberId: Long?,
        cursor: Long?,
    ): List<TodoWithCommentsResponseDto> {
        if (memberId != null && socialMemberId != null) {
            throw IllegalArgumentException(
                "memberId and socialMemberId can not be not-null at the same time.",
            )
        }
        val todos =
            todoRepository.findPageFromCursorByWriterId(
                cursor = cursor,
                sortDirection = sortDirection,
                memberId = memberId,
                socialMemberId = socialMemberId,
            )
        val comments =
            commentRepository.findByTodoIdIn(todos.map { it.id!! })

        return todos.map { it.toWithCommentsResponseDto(comments.map { it.toResponseDto() }) }
    }

    override fun getTodoById(todoId: Long): TodoWithCommentsResponseDto {
        val todo = getTodoOrThrow(todoId)
        val comments = commentRepository.findAllByTodoId(todoId)
        val commentResponses = comments.map { it.toResponseDto() }
        return todo.toWithCommentsResponseDto(commentResponses)
    }

    @Transactional
    override fun createTodo(
        principal: MemberPrincipal,
        createTodoRequest: CreateTodoRequestDto,
    ): TodoResponseDto {
        val (title, content) = createTodoRequest
        val (id, oAuth2Provider, _) = principal

        return if (oAuth2Provider == null) {
            memberRepository.findByIdOrNull(id)?.let {
                Todo(title = title, content = content, member = it)
            } ?: throw ModelNotFoundException("Member", id)
        }
        else {
            socialMemberRepository.findByIdOrNull(id)?.let {
                Todo(title = title, content = content, socialMember = it)
            } ?: throw ModelNotFoundException("SocialMember", id)
        }.let { todoRepository.save(it).toResponseDto() }
    }

    @Transactional
    override fun updateTodo(
        principal: MemberPrincipal,
        todoId: Long,
        updateTodoRequest: UpdateTodoRequestDto,
    ): TodoResponseDto {
        val todo = getTodoOrThrow(todoId)

        assertUserIsTodoWriter(principal, todo)

        val (title, content) = updateTodoRequest
        todo.update(title, content)

        return todo.toResponseDto()
    }

    @Transactional
    override fun updateTodoStatus(
        principal: MemberPrincipal,
        todoId: Long,
        updateTodoStatusRequest: UpdateTodoStatusRequestDto,
    ): TodoResponseDto {
        val todo = getTodoOrThrow(todoId)

        assertUserIsTodoWriter(principal, todo)

        val (status) = updateTodoStatusRequest

        todo.updateStatus(status)

        return todo.toResponseDto()
    }

    @Transactional
    override fun deleteTodo(
        principal: MemberPrincipal,
        todoId: Long,
    ) {
        val todo = getTodoOrThrow(todoId)
        assertUserIsTodoWriter(principal, todo)
        val comments = commentRepository.findAllByTodoId(todoId)
        commentRepository.deleteAll(comments)
        todoRepository.delete(todo)
    }

    private fun assertUserIsTodoWriter(
        principal: MemberPrincipal,
        todo: Todo,
    ) {
        val (id, oAuth2Provider, _) = principal
        if (oAuth2Provider == null) {
            if (id != todo.member!!.id!!) {
                throw IllegalStateException(
                    "Member $id is not the writer of Todo ${todo.id}.",
                )
            }
        }
        else {
            if (id != todo.socialMember!!.id!!) {
                throw IllegalStateException(
                    "SocialMember $id is not the writer of Todo ${todo.id}.",
                )
            }
        }
    }

    private fun getTodoOrThrow(todoId: Long): Todo {
        return todoRepository.findByIdOrNull(todoId)
            ?: throw ModelNotFoundException("Todo", todoId)
    }
}
