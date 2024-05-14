package com.teamsparta.todo.domain.todo.model

import com.teamsparta.todo.domain.todo.dto.TodoResponse
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SourceType
import java.time.ZonedDateTime

@Entity
@Table(name = "todo")
class Todo(
    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "writer", nullable = false, updatable = false)
    var writer: String,

    @field:CreationTimestamp(source = SourceType.DB)
    @Column(name = "createdAt", nullable = false, updatable = false)
    val createdAt: ZonedDateTime? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(newTitle: String, newWriter: String, newContent: String) {
        this.title = newTitle
        this.writer = newWriter
        this.content = newContent
    }

}

fun Todo.toResponse(): TodoResponse {

    return TodoResponse(
        id = id!!,
        title = title,
        content = content,
        writer = writer,
        createdAt = createdAt!!.toOffsetDateTime().toString(),
    )
}
