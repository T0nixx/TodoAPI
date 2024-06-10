package com.teamsparta.todo.domain.todo.model

import com.teamsparta.todo.domain.member.model.Member
import com.teamsparta.todo.domain.member.model.MemberRole
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant

@ExtendWith(MockKExtension::class)
class TodoTest : BehaviorSpec(
    {
        extension(SpringExtension)

        afterContainer {
            clearAllMocks()
        }

        val todoId = 10L
        val title = "Test Title"
        val content = "Test Content"

        Given(
            "올바른 member 혹은 socialMember가 주어졌을 때",
        ) {
            When("Todo 를 생성하면") {
                Then("입력된 값을 바탕으로 Todo가 반환되어야한다.") {
                    val email = "test@test.com"
                    val password = "123123"
                    val role = MemberRole.USER
                    val nickname = "Test Nickname"
                    val memberId = 5L
                    val member =
                        Member(
                            email = email,
                            password = password,
                            role = role,
                            nickname = nickname,
                        ).also { it.id = memberId }

                    val todo = Todo(
                        title = title,
                        content = content,
                        member = member,
                        socialMember = null,
                    ).also { it.id = todoId }

                    todo.id shouldBe todoId
                    todo.title shouldBe title
                    todo.content shouldBe content
                    todo.status shouldBe TodoStatus.TODO
                    todo.createdAt shouldBeLessThan Instant.now()
                    todo.member shouldBe member
                    todo.socialMember shouldBe null
                }
            }
        }

        Given("member와 socialMember가 모두 null로 주어지고") {
            When("Todo를 생성하면") {
                Then("IllegalArgumentException이 발생한다.") {
                    val errorMessage =
                        "Either member or social_member must be set."

                    shouldThrow<IllegalArgumentException> {
                        Todo(
                            title = title,
                            content = content,
                            member = null,
                            socialMember = null,
                        ).also { it.id = todoId }
                    }.message shouldBe errorMessage
                }
            }
        }
    },
)
