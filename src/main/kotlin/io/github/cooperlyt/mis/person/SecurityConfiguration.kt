package org.example.io.github.cooperlyt.mis.person

import io.github.cooperlyt.commons.cloud.keycloak.auth.ReactiveKeycloakAuthenticationConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.*
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
@Configuration
class SecurityConfiguration {

    private val AUTH_LIST = arrayOf( // -- swagger ui
        "/swagger-ui.html",
        "/swagger-ui/*",
        "/swagger-resources/**",
        "/v2/api-docs",
        "/v3/api-docs",
        "/webjars/**",


        "/actuator/**",

        "/db/**",
    )

    @Bean
    @Throws(Exception::class)
    fun webFluxSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange { exchanges: AuthorizeExchangeSpec ->
                exchanges
                    .pathMatchers(*AUTH_LIST).permitAll()
                    .pathMatchers(HttpMethod.GET,"/people/card/**").permitAll()
                    .pathMatchers(HttpMethod.PUT,"/people/card")
                    .hasAnyRole("default-roles-construction","corp-joint")
                    .anyExchange().permitAll()
            }
            .csrf { it.disable()}
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt {
                   it.jwtAuthenticationConverter(ReactiveKeycloakAuthenticationConverter())
                }
            }
            .build()
    }
}