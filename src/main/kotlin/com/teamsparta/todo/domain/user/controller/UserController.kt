package com.teamsparta.todo.domain.user.controller

import com.teamsparta.todo.domain.user.dto.SignInRequestDto
import com.teamsparta.todo.domain.user.dto.SignInResponseDto
import com.teamsparta.todo.domain.user.dto.SignUpRequestDto
import com.teamsparta.todo.domain.user.dto.UserResponseDto
import com.teamsparta.todo.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val userService: UserService) {
    @PostMapping("sign-up")
    fun signUp(
        @RequestBody
        signUpRequestDto: SignUpRequestDto,
    ): ResponseEntity<UserResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.signUp(signUpRequestDto))
    }

    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody
        signInRequestDto: SignInRequestDto,
    ): ResponseEntity<SignInResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.signIn(signInRequestDto))
    }

}
