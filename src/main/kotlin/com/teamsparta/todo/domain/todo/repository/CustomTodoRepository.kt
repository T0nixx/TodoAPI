package com.teamsparta.todo.domain.todo.repository

import com.teamsparta.todo.domain.todo.model.Todo
import org.springframework.data.domain.Sort.Direction

interface CustomTodoRepository {
    fun findPage(sortDirection: Direction): List<Todo>
    fun findPageByWriterId(
        memberId: Long?,
        socialMemberId: Long?,
        sortDirection: Direction,
    ): List<Todo>

    fun findPageFromCursor(
        cursor: Long,
        sortDirection: Direction,
    ): List<Todo>

    fun findPageFromCursorByWriterId(
        cursor: Long?,
        memberId: Long?,
        socialMemberId: Long?,
        sortDirection: Direction,
    ): List<Todo>
}
