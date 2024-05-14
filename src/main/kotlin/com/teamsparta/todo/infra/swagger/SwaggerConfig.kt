package com.teamsparta.todo.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .components(
            Components(),
        ).info(
            Info()
                .title("TODO API")
                .description("TODO API schema")
                .version("1.0.0"),
        )
}
