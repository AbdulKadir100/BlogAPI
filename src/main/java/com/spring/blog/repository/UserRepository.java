package com.spring.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.blog.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	//Optional<User> findByEmail(String email);

	Optional<User> findByNameOrEmail(String username, String email);

	//Optional<User> findByName(String username);

	Boolean existsByName(String username);

	Boolean existsByEmail(String email);

}
