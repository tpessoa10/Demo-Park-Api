package com.mballen.demo_park_api.web.controller;

import com.mballen.demo_park_api.jwt.JwtToken;
import com.mballen.demo_park_api.jwt.JwtUserDetailsService;
import com.mballen.demo_park_api.web.controller.dto.UsuarioLoginDto;
import com.mballen.demo_park_api.web.controller.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class AutenticacaoController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request) {
        // ESTE PRINT TEM QUE APARECER
        System.out.println(">>> [CONTROLLER] Recebido do Postman:");
        System.out.println("    Username: [" + dto.getUsername() + "]");
        System.out.println("    Password: [" + dto.getPassword() + "]");

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException ex) {
            // ESTE PRINT VAI DIZER O ERRO REAL
            System.out.println(">>> ERRO DE LOGIN: " + ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorMessage(request, HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));
        }
    }
}
