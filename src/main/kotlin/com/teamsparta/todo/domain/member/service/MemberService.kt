package com.teamsparta.todo.domain.member.service

import com.teamsparta.todo.domain.member.dto.MemberResponseDto
import com.teamsparta.todo.domain.member.dto.SignInRequestDto
import com.teamsparta.todo.domain.member.dto.SignInResponseDto
import com.teamsparta.todo.domain.member.dto.SignUpRequestDto

interface MemberService {
    fun signUp(signUpRequestDto: SignUpRequestDto): MemberResponseDto

    fun signIn(signInRequestDto: SignInRequestDto): SignInResponseDto
}
