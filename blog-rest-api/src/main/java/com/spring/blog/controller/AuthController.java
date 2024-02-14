package com.spring.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.blog.payload.JWTAuthResponse;
import com.spring.blog.payload.LoginDto;
import com.spring.blog.payload.RegisterDto;
import com.spring.blog.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "CRUD REST APIs for Authentication Resource(Auth Controller)")
public class AuthController {
	
	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	// Log in REST API
	@PostMapping(value = {"/login","/signin"})
	public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
	    // Call the login method from the authService, passing the LoginDto
	    String token = authService.login(loginDto);
	    
	    // Create a JWTAuthResponse and set the access token
	    JWTAuthResponse response = new JWTAuthResponse();
	    response.setAccessToken(token);
	    
	    // Return a ResponseEntity with the JWTAuthResponse and HTTP status code 200 (OK)
	    return ResponseEntity.ok(response);
	}
		
	// Register REST API
	@PostMapping(value = {"/register","/signup"})
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
	    // Call the register method from the authService, passing the RegisterDto
	    String response = authService.register(registerDto);
	    
	    // Create a ResponseEntity with the response message and HTTP status code 201 (CREATED)
	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	

}
