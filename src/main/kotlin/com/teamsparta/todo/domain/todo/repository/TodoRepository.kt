package com.teamsparta.todo.domain.todo.repository

import com.teamsparta.todo.domain.todo.model.Todo
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TodoRepository : JpaRepository<Todo, Long> {
    @Query("select t from Todo t where t.writer = :writer")
    fun findAllByWriter(
        writer: String,
        pageable: Pageable,
    ): List<Todo>

    @Query("select t from Todo t where t.id < :lastTodoId and t.writer = :writer order by t.createdAt desc")
    fun findNextTodoPageByWriter(
        lastTodoId: Long,
        writer: String,
        pageable: Pageable,
    ): List<Todo>

    @Query("select t from Todo t where t.id > :lastTodoId and t.writer = :writer order by t.createdAt asc")
    fun findPreviousTodoPageByWriter(
        lastTodoId: Long,
        writer: String,
        pageable: Pageable,
    ): List<Todo>

    @Query("select t from Todo t where t.id < :lastTodoId order by t.createdAt desc")
    fun findNextTodoPage(
        lastTodoId: Long,
        pageable: Pageable,
    ): List<Todo>

    @Query("select t from Todo t where t.id > :lastTodoId order by t.createdAt asc")
    fun findPreviousTodoPage(
        lastTodoId: Long,
        pageable: Pageable,
    ): List<Todo>
}

