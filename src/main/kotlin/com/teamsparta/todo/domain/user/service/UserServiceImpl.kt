package com.teamsparta.todo.domain.user.service

import com.teamsparta.todo.domain.security.PasswordEncoder
import com.teamsparta.todo.domain.user.dto.SignInRequestDto
import com.teamsparta.todo.domain.user.dto.SignInResponseDto
import com.teamsparta.todo.domain.user.dto.SignUpRequestDto
import com.teamsparta.todo.domain.user.dto.UserResponseDto
import com.teamsparta.todo.domain.user.model.User
import com.teamsparta.todo.domain.user.model.toResponseDto
import com.teamsparta.todo.domain.user.model.toSignInResponseDto
import com.teamsparta.todo.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
) : UserService {

    @Transactional
    override fun signUp(
        signUpRequestDto: SignUpRequestDto,
    ): UserResponseDto {
        val (email, password, username) = signUpRequestDto
        if (userRepository.existsByEmail(email)) {
            throw IllegalStateException("Email: $email already exists")
        }
        val (hashed, salt) = passwordEncoder.encode(password)
        return userRepository.save(
            User(
                email = email,
                password = hashed,
                username = username,
                salt = salt,
            ),
        ).toResponseDto()
    }

    @Transactional
    override fun signIn(signInRequestDto: SignInRequestDto): SignInResponseDto {
        val (email, password) = signInRequestDto
        val user =
            userRepository.findByEmail(email)
                ?: throw IllegalStateException("Email or password is incorrect")
        if (passwordEncoder.matches(
                target = password,
                salt = user.salt,
                hashed = user.password,
            ) == false
        ) {
            throw IllegalStateException("Email or password is incorrect")
        }

        return user.toSignInResponseDto()
    }
}
