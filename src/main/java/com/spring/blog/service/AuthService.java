package com.spring.blog.service;

import com.spring.blog.payload.LoginDto;
import com.spring.blog.payload.RegisterDto;

public interface AuthService {
	String login(LoginDto loginDto);
	String register(RegisterDto registerDto);

}
