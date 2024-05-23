package com.teamsparta.todo.domain.user.service

import com.teamsparta.todo.domain.user.dto.SignInRequestDto
import com.teamsparta.todo.domain.user.dto.SignInResponseDto
import com.teamsparta.todo.domain.user.dto.SignUpRequestDto
import com.teamsparta.todo.domain.user.dto.UserResponseDto

interface UserService {
    fun signUp(signUpRequestDto: SignUpRequestDto): UserResponseDto

    fun signIn(signInRequestDto: SignInRequestDto): SignInResponseDto
}
