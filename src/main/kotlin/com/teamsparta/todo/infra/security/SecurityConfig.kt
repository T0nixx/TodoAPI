package com.teamsparta.todo.infra.security

import com.teamsparta.todo.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@EnableMethodSecurity
@Configuration
class SecurityConfig {
    private val allowedUrls =
        arrayOf(
            "/",
            "/swagger-ui/**",
            "/v3/**",
            "/sign-up",
            "/sign-in",
            "/oauth2/**",
        )

    @Autowired
    lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @Bean
    fun filterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        return http
            .csrf {
                it.disable()
            }
            .headers {
                it.frameOptions { frameOptions ->
                    frameOptions.sameOrigin()
                }
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers(*allowedUrls)
                    .permitAll()
                    .requestMatchers(HttpMethod.GET)
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS,
                )
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                BasicAuthenticationFilter::class.java,
            )
            .build()!!
    }
}
