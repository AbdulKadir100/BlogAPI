package com.spring.blog.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.spring.blog.exception.BlogAPIException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	
	// these below two properties should be defined in the app.properties file
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpirationDate;
	
	
	
	// Method to generate a JWT token for the given authentication
	public String generateToken(Authentication authentication) {
	    // Get the username from the authentication object
	    String username = authentication.getName();
	    
	    // Get the current date
	    Date currentDate = new Date();
	    
	    // Calculate the expiration date for the token by adding the expiration duration to the current date
	    Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
	    
	    // Generate the JWT token with the specified claims and sign it with the key
	    String token = Jwts.builder()
	            // Set the subject (username) of the token
	            .setSubject(username)
	            // Set the issued date of the token to the current date
	            .setIssuedAt(new Date())
	            // Set the expiration date of the token
	            .setExpiration(expireDate)
	            // Sign the token with the cryptographic key
	            .signWith(key())
	            // Compact the token into its final string representation
	            .compact();
	    
	    // Return the generated token
	    return token;
	}
	
	// Method to generate a cryptographic key for signing and verifying JWTs
	private Key key() {
	    // Generate a HMAC-SHA cryptographic key using the provided JWT secret
	    SecretKey secretKey = Keys.hmacShaKeyFor(
	            // Decode the JWT secret from Base64 format
	            // Decoders.BASE64.decode() decodes the Base64 string into bytes
	            Decoders.BASE64.decode(jwtSecret));
	    
	    // Return the cryptographic key
	    return secretKey;
	}

	
	// Method to extract the username from a JWT token
	public String getUsername(String token) {
	    // Parse the JWT token using the parser builder
	    Claims claims =  (Claims) Jwts.parserBuilder()
	            // Set the signing key used for verifying the token signature
	            .setSigningKey(key())
	            // Build the parser
	            .build()
	            // Parse the token and retrieve the claims
	            .parse(token)
	            // Get the body of the token, which contains the claims
	            .getBody();

	    // Get the username from the claims
	    String username = claims.getSubject();
	    
	    // Return the username
	    return username;
	}
	
	// validate JWT token
	public boolean validateToken(String token) {
		try {
	        // Parse the JWT token using the parser builder and verify the signature
	        Jwts.parserBuilder()
	            .setSigningKey(key())
	            .build()
	            .parse(token);
	        
	        // If the token is successfully parsed and verified, return true
	        return true;
	    } catch (MalformedJwtException ex) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
		}catch (ExpiredJwtException ex) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
		}catch (UnsupportedJwtException ex) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
		}catch (IllegalArgumentException ex) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
		}
		
	}
}
