package com.teamsparta.todo.domain.todo.model

import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import com.teamsparta.todo.domain.user.model.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: TodoStatus = TodoStatus.TODO,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    var writer: User,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(newTitle: String, newContent: String) {
        this.title = newTitle
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

fun Todo.toResponseDto(): TodoResponseDto {

    return TodoResponseDto(
        id = id!!,
        title = title,
        content = content,
        writerId = writer.id!!,
        status = status.name,
        createdAt = createdAt.toString(),
    )
}

fun Todo.toWithCommentsResponseDto(comments: List<CommentResponseDto>): TodoWithCommentsResponseDto {

    return TodoWithCommentsResponseDto(
        id = id!!,
        title = title,
        content = content,
        writerId = writer.id!!,
        createdAt = createdAt.toString(),
        status = status.name,
        comments = comments,
    )
}
