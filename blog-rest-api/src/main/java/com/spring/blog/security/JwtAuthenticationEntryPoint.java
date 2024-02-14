package com.spring.blog.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Indicates that this class is a Spring component
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

 // This method is called when an unauthenticated user tries to access a secured resource
 @Override
 public void commence(HttpServletRequest request, 
                      HttpServletResponse response,
                      AuthenticationException authException) throws IOException, ServletException {
     
     // Send an HTTP error response with status code SC_UNAUTHORIZED (401)
     // This indicates that the request lacks valid authentication credentials
     response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
 }
}
