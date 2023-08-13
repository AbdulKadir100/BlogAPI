package com.spring.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
	private long id;
	
	// name should not be null or empty
	@NotEmpty(message = "Name should not be null or empty")
	private String name;
	
	
	// email should not be null or empty
	// email field validation
	@NotEmpty(message = "email should not be null or empty")
	@Email
    private String email;
	
	// comment body should not be null or empty
	// comment body must have at least 10 chars
	@NotEmpty(message = "body should not be null or empty")
	@Size(min = 10, message = "comment body must have at least 10 chars")
	private String body;

}
