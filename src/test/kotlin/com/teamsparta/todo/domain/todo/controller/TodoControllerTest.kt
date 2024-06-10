package com.teamsparta.todo.domain.todo.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.teamsparta.todo.domain.exception.dto.ErrorResponse
import com.teamsparta.todo.domain.exception.dto.ModelNotFoundException
import com.teamsparta.todo.domain.member.model.MemberRole
import com.teamsparta.todo.domain.todo.dto.CreateTodoRequestDto
import com.teamsparta.todo.domain.todo.dto.TodoResponseDto
import com.teamsparta.todo.domain.todo.service.TodoService
import com.teamsparta.todo.infra.security.dto.MemberPrincipal
import com.teamsparta.todo.infra.security.jwt.JwtProvider
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
class TodoControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val jwtProvider: JwtProvider,
    @MockkBean
    private val todoService: TodoService,
) : DescribeSpec(
    {
        extension(SpringExtension)
        afterContainer {
            clearAllMocks()
        }

        //        val todoService = mockk<TodoService>(block = {})
        describe("POST /todos는") {
            context("올바른 입력이 들어왔을 때") {
                it("201 status code로 응답해야한다.") {
                    val createdAt = Instant.now().toString()
                    // given
                    val memberId = 1L
                    val memberRole = MemberRole.USER
                    val title = "tilte"
                    val content = "content"
                    val createdTodoId = 10L

                    every {
                        todoService.createTodo(
                            MemberPrincipal(
                                id = memberId,
                                oAuth2Provider = null,
                                roles = setOf(memberRole),
                            ),
                            CreateTodoRequestDto(
                                title = title,
                                content = content,
                            ),
                        )
                    } returns TodoResponseDto(
                        id = 10L,
                        content = "Test Content",
                        memberId = 1L,
                        socialMemberId = null,
                        status = "TODO",
                        createdAt = createdAt,
                        title = "Test Todo",
                    )

                    val jwtToken = jwtProvider.createToken(
                        id = memberId,
                        oAuth2Provider = null,
                        role = memberRole,
                    )
                    val request = CreateTodoRequestDto(
                        title = title,
                        content = content,
                    )

                    val result = mockMvc.perform(
                        post("/todos")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer $jwtToken")
                            .content(
                                jacksonObjectMapper().writeValueAsString(
                                    request,
                                ),
                            ),
                    ).andReturn()

                    result.response.status shouldBe 201

                    val responseDto = jacksonObjectMapper().readValue(
                        result.response.contentAsString,
                        TodoResponseDto::class.java,
                    )

                    responseDto.id shouldBe createdTodoId
                }
            }
        }

        describe("GET /todos/{todoId}는") {
            context("존재하지 않는 todoId에 대해 조회할 경우") {
                it("ModelNotFoundException에 대한 message가 담긴 ErrorResponse로 응답해야 한다.") {
                    // given
                    val todoId = 170L

                    // when
                    every {
                        todoService.getTodoById(
                            todoId,
                        )
                    } throws ModelNotFoundException("Todo", todoId)

                    // then
                    val result = mockMvc.perform(
                        get("/todos/$todoId")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON),
                    ).andReturn()

                    result.response.status shouldBe 400

                    val responseDto = jacksonObjectMapper().readValue(
                        result.response.contentAsString,
                        ErrorResponse::class.java,
                    )

                    responseDto.message shouldBe "Model Todo not found with given id $todoId"
                }
            }
        }
    },
)

