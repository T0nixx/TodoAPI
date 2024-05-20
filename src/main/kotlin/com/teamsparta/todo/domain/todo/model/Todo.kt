package com.teamsparta.todo.domain.todo.model

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponse
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "todo")
class Todo(
    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "writer", nullable = false, updatable = false)
    var writer: String,

    @CreatedDate
    @Column(name = "createdAt", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: TodoStatus = TodoStatus.TODO,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(newTitle: String, newWriter: String, newContent: String) {
        this.title = newTitle
        this.writer = newWriter
        this.content = newContent
    }

    fun updateStatus(newStatusString: String) {
        val newStatus =
            enumValues<TodoStatus>().find { it.name == newStatusString }
                ?: throw IllegalArgumentException("$newStatusString is invalid status.")
        if (newStatus == this.status) {
            throw IllegalArgumentException("New status: $newStatusString is same with old one.")
        }

        this.status = newStatus
    }
}

fun Todo.toResponse(): TodoResponse {

    return TodoResponse(
        id = id!!,
        title = title,
        content = content,
        writer = writer,
        status = status.name,
        createdAt = createdAt.toString(),
    )
}

fun Todo.toWithCommentsResponse(comments: List<CommentResponse>): TodoWithCommentsResponse {

    return TodoWithCommentsResponse(
        id = id!!,
        title = title,
        content = content,
        writer = writer,
        createdAt = createdAt.toString(),
        status = status.name,
        comments = comments,
    )
}
