package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.AddCommentRequestDto
import com.teamsparta.todo.domain.comment.dto.CommentResponseDto
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequestDto
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.member.repository.MemberRepository
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
) : CommentService {
    @Transactional
    override fun addComment(
        memberId: Long,
        todoId: Long,
        addCommentRequest: AddCommentRequestDto,
    ): CommentResponseDto {
        val todo = getTodoOrThrow(todoId)

        val (content) = addCommentRequest
        return memberRepository.findByIdOrNull(memberId)?.let { member ->
            Comment(
                member = member,
                todo = todo,
                content = content,
            ).let {
                commentRepository.save(it)
            }.let {
                CommentResponseDto.from(it)
            }
        } ?: throw ModelNotFoundException("Member", memberId)
    }

    @Transactional
    override fun updateComment(
        memberId: Long,
        todoId: Long,
        commentId: Long,
        updateCommentRequest: UpdateCommentRequestDto,
    ): CommentResponseDto {
        val todo = getTodoOrThrow(todoId)

        val comment = getCommentOrThrow(commentId)

        checkCommentBelongsToTodo(comment, todo)
        checkUserIsCommentWriter(memberId, comment)
        val (content) = updateCommentRequest

        comment.updateContent(content)
        return CommentResponseDto.from(comment)
    }

    @Transactional
    override fun deleteComment(memberId: Long, todoId: Long, commentId: Long) {
        val todo = getTodoOrThrow(todoId)
        val comment = getCommentOrThrow(commentId)

        checkCommentBelongsToTodo(comment, todo)
        checkUserIsCommentWriter(memberId, comment)

        commentRepository.delete(comment)
    }

    private fun checkCommentBelongsToTodo(
        comment: Comment,
        todo: Todo,
    ) {
        if (comment.todo.id != todo.id) {
            throw IllegalArgumentException(
                "This Comment (id: $comment.id) does not belong to Todo (id: $todo.id).",
            )
        }
    }

    private fun checkUserIsCommentWriter(
        memberId: Long,
        comment: Comment,
    ) {
        if (memberId != comment.member!!.id!!) {
            throw IllegalStateException(
                "Member $memberId is not the writer of Comment ${comment.id}.",
            )
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
