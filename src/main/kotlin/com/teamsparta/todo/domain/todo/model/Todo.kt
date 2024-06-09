package com.teamsparta.todo.domain.todo.model

import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.member.model.Member
import com.teamsparta.todo.domain.socialmember.model.SocialMember
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentsResponseDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
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
import java.time.Instant

@Entity
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
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_member_id")
    var socialMember: SocialMember? = null,
) {
    init {
        require((member != null && socialMember == null) || (member == null && socialMember != null)) {
            "Either member or social_member must be set."
        }
    }

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
        memberId = member?.id,
        socialMemberId = socialMember?.id,
        status = status.name,
        createdAt = createdAt.toString(),
    )
}

fun Todo.toWithCommentsResponseDto(comments: List<CommentResponseDto>): TodoWithCommentsResponseDto {

    return TodoWithCommentsResponseDto(
        id = id!!,
        title = title,
        content = content,
        memberId = member?.id,
        socialMemberId = socialMember?.id,
        createdAt = createdAt.toString(),
        status = status.name,
        comments = comments,
    )
}
