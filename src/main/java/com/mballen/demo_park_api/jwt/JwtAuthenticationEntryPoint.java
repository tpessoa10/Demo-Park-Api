package com.mballen.demo_park_api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mballen.demo_park_api.web.controller.exception.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.UNAUTHORIZED, "Authentication is required");

        response.setHeader("www-authenticate", "Bearer real='/api/v1/auth'");
        response.setContentType("application/json");
        response.setStatus(401);

        new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);
    }
}
