package com.teamsparta.todo.domain.member.service

import com.teamsparta.todo.domain.member.dto.MemberResponseDto
import com.teamsparta.todo.domain.member.dto.SignInRequestDto
import com.teamsparta.todo.domain.member.dto.SignInResponseDto
import com.teamsparta.todo.domain.member.dto.SignUpRequestDto
import com.teamsparta.todo.domain.member.model.Member
import com.teamsparta.todo.domain.member.model.MemberRole
import com.teamsparta.todo.domain.member.model.toResponseDto
import com.teamsparta.todo.domain.member.model.toSignInResponseDto
import com.teamsparta.todo.domain.member.repository.MemberRepository
import com.teamsparta.todo.infra.security.jwt.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val memberRepository: MemberRepository,
    private val jwtProvider: JwtProvider,
) : MemberService {
    @Transactional
    override fun signUp(
        signUpRequestDto: SignUpRequestDto,
    ): MemberResponseDto {
        val (email, password, nickname) = signUpRequestDto
        if (memberRepository.existsByEmail(email)) {
            throw IllegalStateException("Email: $email already exists")
        }

        return memberRepository.save(
            Member(
                email = email,
                password = passwordEncoder.encode(password),
                nickname = nickname,
                role = MemberRole.USER,
            ),
        ).toResponseDto()
    }

    @Transactional
    override fun signIn(
        signInRequestDto: SignInRequestDto,
    ): SignInResponseDto {
        val (email, password) = signInRequestDto
        val member =
            memberRepository.findByEmail(email)
                ?: throw IllegalStateException("Email or password is incorrect")
        if (passwordEncoder.matches(password, member.password) == false) {
            throw IllegalStateException("Email or password is incorrect")
        }
        val token =
            jwtProvider.createToken(member.id!!, member.role, null)
        return member.toSignInResponseDto(token)
    }
}
