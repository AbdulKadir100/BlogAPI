package com.spring.blog.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.spring.blog.entity.Post;
import com.spring.blog.entity.Role;

@DataJpaTest
public class PostRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;
	
	// Junit test for save post
	@DisplayName("Junit test for save post")
	@Test
	public void givenPostObject_whenSave_thenReturnSavedEmployee() {
		
		//given - precondition or setup
		Role role = new Role();
		role.setName("User");
		
		//when - action or the behavior that we are going test
		//Post savedPost = postRepository.save(post);
		Role savedRole = roleRepository.save(role);
		
		// then - verify the output
		Assertions.assertThat(savedRole).isNotNull();
		//Assertions.assertThat(savedPost.getId()).isGreaterThan(0);
		
	}

}
