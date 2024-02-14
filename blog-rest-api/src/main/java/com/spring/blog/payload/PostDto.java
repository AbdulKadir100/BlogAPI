package com.spring.blog.payload;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class PostDto {
	private long id;

	// title should not be null or empty
	// title should have at least 2 chars
	@NotEmpty
	@Size(min = 2, message = "Title should have at least 2 characters")
	private String title;

	// Description should not be null or empty
	// Description should have at least 10 chars
	@NotEmpty
	@Size(min = 10, message = "Description should have at least 10 characters")
	private String description;
	
	// Content should not be empty
	@NotEmpty
	private String content;
	// it will show comment with post
	// for that remove toString from post class
	private Set<CommentDto> comments;
	
	// related to category
	private Long categoryId;

}
