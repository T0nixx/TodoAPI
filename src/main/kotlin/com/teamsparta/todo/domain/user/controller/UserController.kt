package com.teamsparta.todo.domain.user.controller

import com.teamsparta.todo.domain.user.dto.SignInRequestDto
import com.teamsparta.todo.domain.user.dto.SignInResponseDto
import com.teamsparta.todo.domain.user.dto.SignUpRequestDto
import com.teamsparta.todo.domain.user.dto.UserResponseDto
import com.teamsparta.todo.domain.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/users")
class UserController(val userService: UserService) {
    @PostMapping("/sign-up")
    fun signUp(
        @Valid
        @RequestBody
        signUpRequestDto: SignUpRequestDto,
    ): ResponseEntity<UserResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.signUp(signUpRequestDto))
    }

    @PostMapping("/sign-in")
    fun signIn(
        @Valid
        @RequestBody
        signInRequestDto: SignInRequestDto,
    ): ResponseEntity<SignInResponseDto> {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.signIn(signInRequestDto))
    }

}
