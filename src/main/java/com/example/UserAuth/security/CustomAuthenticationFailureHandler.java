package com.example.UserAuth.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid username or password. Please try again.";

        if (exception instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password. Please try again.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "An internal error occurred. Please try again later.";
        } else {
            errorMessage = "An unexpected error occurred. Please try again.";
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login?error=true");
    }
}
