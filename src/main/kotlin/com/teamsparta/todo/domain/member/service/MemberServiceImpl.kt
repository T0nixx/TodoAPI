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
import com.teamsparta.todo.domain.security.JwtProvider
import com.teamsparta.todo.domain.security.PasswordEncoder
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
        val (email, password, username) = signUpRequestDto
        if (memberRepository.existsByEmailOrUsername(email, username)) {
            throw IllegalStateException("Email: $email or Username: $username already exists")
        }
        val (hashed, salt) = passwordEncoder.encode(password)


        return memberRepository.save(
            Member(
                email = email,
                password = hashed,
                username = username,
                salt = salt,
                role = MemberRole.USER,
            ),
        ).toResponseDto()
    }

    @Transactional
    override fun signIn(
        signInRequestDto: SignInRequestDto,
    ): SignInResponseDto {
        val (email, password) = signInRequestDto
        val user =
            memberRepository.findByEmail(email)
                ?: throw IllegalStateException("Email or password is incorrect")
        if (passwordEncoder.matches(
                target = password,
                salt = user.salt,
                hashed = user.password,
            ) == false
        ) {
            throw IllegalStateException("Email or password is incorrect")
        }
        val token =
            jwtProvider.createToken("${user.id}:${user.password}:${user.role}}")
        return user.toSignInResponseDto(token)
    }
}
