package com.teamsparta.todo.domain.todo.repository

import com.teamsparta.todo.domain.todo.model.Todo
import org.springframework.data.domain.Sort.Direction

interface CustomTodoRepository {
    fun findPage(sortDirection: Direction): List<Todo>
    fun findPageByWriterId(
        writerId: Long,
        sortDirection: Direction,
    ): List<Todo>

    fun findPageFromCursor(
        cursor: Long,
        writerId: Long?,
        sortDirection: Direction,
    ): List<Todo>
}
