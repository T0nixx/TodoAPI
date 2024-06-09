package com.teamsparta.todo.domain.member.controller

import com.teamsparta.todo.domain.member.dto.MemberResponseDto
import com.teamsparta.todo.domain.member.dto.SignInRequestDto
import com.teamsparta.todo.domain.member.dto.SignInResponseDto
import com.teamsparta.todo.domain.member.dto.SignUpRequestDto
import com.teamsparta.todo.domain.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/members")
class MemberController(val memberService: MemberService) {
    @PostMapping("/sign-up")
    fun signUp(
        @Valid
        @RequestBody
        signUpRequestDto: SignUpRequestDto,
    ): ResponseEntity<MemberResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(memberService.signUp(signUpRequestDto))
    }

    @PostMapping("/sign-in")
    fun signIn(
        @Valid
        @RequestBody
        signInRequestDto: SignInRequestDto,
    ): ResponseEntity<SignInResponseDto> {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(memberService.signIn(signInRequestDto))
    }
}
