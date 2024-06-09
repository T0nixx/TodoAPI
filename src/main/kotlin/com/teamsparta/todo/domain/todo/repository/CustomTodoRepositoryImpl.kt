package com.teamsparta.todo.domain.todo.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamsparta.todo.domain.member.model.QMember
import com.teamsparta.todo.domain.todo.model.QTodo
import com.teamsparta.todo.domain.todo.model.Todo
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Repository
import java.time.Instant

private const val PAGE_SIZE = 10L

@Repository
class CustomTodoRepositoryImpl(private val queryFactory: JPAQueryFactory) :
    CustomTodoRepository {
    private val todo = QTodo.todo
    private fun getOrderSpecifier(sortDirection: Direction): OrderSpecifier<Instant> {
        return if (sortDirection == Direction.DESC == true) todo.createdAt.desc() else todo.createdAt.asc()
    }

    override fun findPage(sortDirection: Direction): List<Todo> {
        return queryFactory
            .selectFrom(todo)
            .leftJoin(todo.writer, QMember.member)
            .fetchJoin()
            .orderBy(getOrderSpecifier(sortDirection))
            .limit(PAGE_SIZE)
            .fetch()
    }

    override fun findPageByWriterId(
        writerId: Long,
        sortDirection: Direction,
    ): List<Todo> {
        return queryFactory
            .selectFrom(todo)
            .leftJoin(todo.writer, QMember.member)
            .fetchJoin()
            .where(todo.writer.id.eq(writerId))
            .orderBy(getOrderSpecifier(sortDirection))
            .limit(PAGE_SIZE)
            .fetch()
    }

    override fun findPageFromCursor(
        cursor: Long,
        sortDirection: Direction,
    ): List<Todo> {
        val todo = QTodo.todo
        val isDescending = sortDirection == Direction.DESC
        val gtOrLtId =
            if (isDescending == true) todo.id.lt(cursor) else todo.id.gt(cursor)

        return queryFactory
            .selectFrom(todo)
            .leftJoin(todo.writer, QMember.member)
            .fetchJoin()
            .where(gtOrLtId)
            .orderBy(getOrderSpecifier(sortDirection))
            .limit(PAGE_SIZE)
            .fetch()
    }

    override fun findPageFromCursorByWriterId(
        cursor: Long,
        writerId: Long,
        sortDirection: Direction,
    ): List<Todo> {
        val todo = QTodo.todo
        val isDescending = sortDirection == Direction.DESC
        val gtOrLtId =
            if (isDescending == true) todo.id.lt(cursor) else todo.id.gt(cursor)

        return queryFactory
            .selectFrom(todo)
            .leftJoin(todo.writer, QMember.member).fetchJoin()
            .where(gtOrLtId.and(todo.writer.id.eq(writerId)))
            .orderBy(getOrderSpecifier(sortDirection))
            .limit(PAGE_SIZE)
            .fetch()
    }
}
