package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.model.toResponseDto
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.member.repository.MemberRepository
import com.teamsparta.todo.domain.socialmember.repository.SocialMemberRepository
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import com.teamsparta.todo.infra.security.dto.MemberPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val socialMemberRepository: SocialMemberRepository,
) : CommentService {

    @Transactional
    override fun addComment(
        principal: MemberPrincipal,
        todoId: Long,
        addCommentRequest: AddCommentRequestDto,
    ): CommentResponseDto {
        val todo = getTodoOrThrow(todoId)

        val (id, oAuth2Provider, _) = principal
        val (content) = addCommentRequest
        return if (oAuth2Provider == null) {
            memberRepository.findByIdOrNull(id)?.let {
                Comment(
                    member = it,
                    todo = todo,
                    content = content,
                    socialMember = null,
                )
            } ?: throw ModelNotFoundException("Member", id)
        }
        else {
            socialMemberRepository.findByIdOrNull(id)?.let {
                Comment(
                    member = null,
                    todo = todo,
                    content = content,
                    socialMember = it,
                )
            } ?: throw ModelNotFoundException("SocialMember", id)
        }.let { commentRepository.save(it).toResponseDto() }
    }

    @Transactional
    override fun updateComment(
        principal: MemberPrincipal,
        todoId: Long,
        commentId: Long,
        updateCommentRequest: UpdateCommentRequestDto,
    ): CommentResponseDto {
        val todo = getTodoOrThrow(todoId)

        val comment = getCommentOrThrow(commentId)

        assertCommentBelongsToTodo(comment, todo)
        assertUserIsCommentWriter(principal, comment)
        val (content) = updateCommentRequest

        comment.updateContent(content)
        return comment.toResponseDto()
    }

    @Transactional
    override fun deleteComment(
        principal: MemberPrincipal,
        todoId: Long,
        commentId: Long,
    ) {
        val todo = getTodoOrThrow(todoId)
        val comment = getCommentOrThrow(commentId)

        assertCommentBelongsToTodo(comment, todo)
        assertUserIsCommentWriter(principal, comment)

        commentRepository.delete(comment)
    }

    private fun assertCommentBelongsToTodo(comment: Comment, todo: Todo) {
        if (comment.todo.id != todo.id) {
            throw IllegalArgumentException("This Comment (id: $comment.id) does not belong to Todo (id: $todo.id).")
        }
    }

    private fun assertUserIsCommentWriter(
        principal: MemberPrincipal,
        comment: Comment,
    ) {
        val (id, oAuth2Provider, _) = principal
        if (oAuth2Provider == null) {
            if (id != comment.member!!.id!!) throw IllegalStateException("Member $id is not the writer of Comment ${comment.id}.")
        }
        else {
            if (id != comment.socialMember!!.id!!) throw IllegalStateException("SocialMember $id is not the writer of Comment ${comment.id}.")
        }
    }

    private fun getTodoOrThrow(todoId: Long): Todo {
        return todoRepository.findByIdOrNull(todoId)
            ?: throw ModelNotFoundException("Todo", todoId)
    }

    private fun getCommentOrThrow(commentId: Long): Comment {
        return commentRepository.findByIdOrNull(commentId)
            ?: throw ModelNotFoundException("Comment", commentId)
    }
}
