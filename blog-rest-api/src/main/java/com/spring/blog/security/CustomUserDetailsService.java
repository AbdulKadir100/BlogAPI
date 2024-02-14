package com.spring.blog.security;


import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.spring.blog.entity.User;
import com.spring.blog.repository.UserRepository;

//Indicates that this class is a Spring service bean
@Service
public class CustomUserDetailsService implements UserDetailsService {

 // Declaration of UserRepository field
 private UserRepository userRepository;

 // Constructor for CustomUserDetailsService, UserRepository is injected by Spring
 @Autowired
 public CustomUserDetailsService(UserRepository userRepository) {
     this.userRepository = userRepository;
 }

 // This method is called by Spring Security when attempting to authenticate a user
 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	 
     // Query the database for a user with the given username or email
     User user = userRepository.findByNameOrEmail(username, username)
    		 
             // Throws UsernameNotFoundException if the user is not found
             .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username));
     
     // Get all the roles of the user and map them to GrantedAuthority objects
     Set<GrantedAuthority> authorities = user.getRoles()
             .stream()
             // Map Role objects to SimpleGrantedAuthority objects
             .map((role) -> new SimpleGrantedAuthority(role.getName()))
             // Collect them into a set
             .collect(Collectors.toSet());
     
     // Construct and return a UserDetails object with the user's details
     return new org.springframework.security.core.userdetails.User(
             user.getEmail(), // User's email
             user.getPassword(), // User's password
             authorities); // Set of authorities (roles)
 }
}