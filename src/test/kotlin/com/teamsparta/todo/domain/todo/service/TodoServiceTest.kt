package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.member.model.Member
import com.teamsparta.todo.domain.member.model.OAuth2Provider
import com.teamsparta.todo.domain.member.repository.MemberRepository
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.model.TodoStatus
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.Instant

@SpringBootTest
@ExtendWith(MockKExtension::class)
class TodoServiceTest : BehaviorSpec(
    {
        extension(SpringExtension)

        afterContainer {
            clearAllMocks()
        }

        val todoRepository = mockk<TodoRepository>()
        val commentRepository = mockk<CommentRepository>()
        val memberRepository = mockk<MemberRepository>()

        val todoService =
            TodoServiceImpl(
                todoRepository,
                commentRepository,
                memberRepository,
            )

        Given(
            "todoId가 있을 때",
        ) {
            When("존재하지 않는 todoId에 대해 조회하면") {
                Then("ModelNotFoundException이 발생해야 한다.") {
                    val todoId = 30L
                    every { todoRepository.findByIdOrNull(todoId) } returns null

                    shouldThrow<ModelNotFoundException> {
                        todoService.getTodoById(todoId)
                    }
                }
            }
        }

        Given(
            "특정 todoId에 대하여",
        ) {
            When("존재하는 todoId에 대해 조회하면") {
                Then("TodoWithCommentsResponseDto를 반환해야한다.") {
                    val todoId = 5L
                    val title = "TEST"
                    val content = "TEST"
                    val socialMember = Member.createSocialMember(
                        provider = OAuth2Provider.NAVER,
                        providerUserId = "SOME_PROVIDER_USER_ID",
                        nickname = "SOME_NICKNAME",
                    )

                    every { todoRepository.findByIdOrNull(todoId) } returns Todo(
                        title = title,
                        content = content,
                        member = socialMember,
                    ).also { it.id = todoId }

                    every { commentRepository.findAllByTodoId(todoId) } returns emptyList()

                    val result = todoService.getTodoById(todoId)

                    result.id shouldBe todoId
                    result.title shouldBe title
                    result.content shouldBe content
                    result.createdAt shouldBeLessThan Instant.now().toString()
                    result.comments shouldBe emptyList()
                    result.memberId shouldBe socialMember.id
                    result.status shouldBe TodoStatus.TODO.name
                }
            }
        }
    },
)
