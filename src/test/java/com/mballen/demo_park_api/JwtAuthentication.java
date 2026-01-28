package com.mballen.demo_park_api;

import com.mballen.demo_park_api.jwt.JwtToken;
import com.mballen.demo_park_api.web.controller.dto.UsuarioLoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {
        String token = client.post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult()
                .getResponseBody()
                .getToken();


        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
