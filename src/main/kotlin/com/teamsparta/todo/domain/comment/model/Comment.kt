package com.teamsparta.todo.domain.comment.model

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.todo.model.Todo
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "comment")
class Comment(

    @Column(name = "writer", nullable = false)
    var writer: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    val todo: Todo,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "salt", nullable = false, updatable = false, unique = true)
    var salt: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updateContent(newContent: String) {
        this.content = newContent
    }
}

fun Comment.toResponse(): CommentResponse {

    return CommentResponse(
        id = this.id!!,
        content = this.content,
        writer = this.writer,
    )
}
