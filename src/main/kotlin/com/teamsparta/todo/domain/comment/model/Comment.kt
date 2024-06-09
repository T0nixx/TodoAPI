package com.teamsparta.todo.domain.comment.model

import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.member.model.Member
import com.teamsparta.todo.domain.socialmember.model.SocialMember
import com.teamsparta.todo.domain.todo.model.Todo
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "comment")
class Comment(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_member_id")
    var socialMember: SocialMember?,

    @Column(name = "content", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    val todo: Todo,
) {
    init {
        require((member != null && socialMember == null) || (member == null && socialMember != null)) {
            "Either member or social_member must be set."
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updateContent(newContent: String) {
        content = newContent
    }
}

fun Comment.toResponseDto(): CommentResponseDto {
    return CommentResponseDto(
        id = id!!,
        content = content,
        memberId = member?.id,
        socialMemberId = socialMember?.id,
    )
}
