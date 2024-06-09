package com.teamsparta.todo.oauth2.controller

import com.teamsparta.todo.domain.socialmember.model.OAuth2Provider
import com.teamsparta.todo.oauth2.dto.OAuth2LoginResponseDto
import com.teamsparta.todo.oauth2.service.OAuth2ClientService
import com.teamsparta.todo.oauth2.service.OAuth2LoginService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/oauth2")
@Controller
class OAuth2LoginController(
    private val oAuth2ClientService: OAuth2ClientService,
    private val oAuth2LoginService: OAuth2LoginService,
) {
    @GetMapping("/{provider}/login")
    fun login(
        @PathVariable
        provider: String,
        response: HttpServletResponse,
    ) {
        val providerName = OAuth2Provider.valueOf(provider.uppercase())
        val authorizationUrl =
            oAuth2ClientService.getAuthorizationUrl(providerName)

        response.sendRedirect(authorizationUrl)
    }

    @GetMapping("/{provider}/success")
    fun success(
        @PathVariable
        provider: String,
        @RequestParam
        state: String,
        @RequestParam
        code: String?,
        @RequestParam
        error: String?,
        @RequestParam(name = "error_description")
        errorDescription: String?,
    ): ResponseEntity<OAuth2LoginResponseDto> {
        if (error != null) throw IllegalStateException(errorDescription!!)
        val providerName = OAuth2Provider.valueOf(provider.uppercase())
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(oAuth2LoginService.login(providerName, code!!))
    }
}
