package com.spring.blog.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtTokenProvider jwtTokenProvider;
	private UserDetailsService userDetailsService;
	
    
	@Autowired
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {

		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		
		// Get JWT token from HTTP request
		String token = getTokenFromRequest(request);
		
		// Validate token ,  StringUtils.hastext check whether token is empty or null
		if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			
			// get user name from token
			String username = jwtTokenProvider.getUsername(token);
			
			// get the user object from the database(loading the user associated with token)
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			// 
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					username,
					null,
					userDetails.getAuthorities());
			
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			// set the above authToken to security context holder
		    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		
		filterChain.doFilter(request, response);
		
	}
	
	// This method will extract the token from request
	private String getTokenFromRequest(HttpServletRequest request) {
		
		String bearerToken = request.getHeader("Authorization");
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length()); // length of bearer is 7
		}
		return null;
	}
	
	
}
