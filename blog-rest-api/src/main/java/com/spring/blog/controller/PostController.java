package com.spring.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.blog.payload.PostDto;
import com.spring.blog.payload.PostResponse;
import com.spring.blog.service.PostService;
import com.spring.blog.utils.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "CRUD REST APIs for Post Resource(Post Controller)")
public class PostController {

	// private PostServiceImpl postServiceImpl;
	// we are using here interface instead of class, loose coupled via interface
	private PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@Parameter(
			name = "Post Entity",
			description = "Title, Description and Content")
	@Operation(
			summary = "Create Post REST API",
			description = "Create Post REST API is used to save post into database"
			)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status 201 CREATED"
			)
	// for swagger docs.
	@SecurityRequirement(
			name = "Bearer Authentication"
			)
	// create post rest API for blog
	// only admin can access this method
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
		return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
	}

	
	@Operation(
			summary = "Get All Post REST API",
			description = "Get All Post REST API is used to fecth all posts from the database"
			)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 OK"
			)
	// Get all posts rest api and applying pagination and sorting
	@GetMapping
	public List<PostDto> getAllPosts(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
		return postService.getAllPosts(pageNo, pageSize);
	}

	@Operation(
			summary = "Get All Post REST API",
			description = "Get All Post REST API is used to fecth all posts from the database in sorted order"
			)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 OK"
			)
	// Get all posts rest API and applying sorting
	@GetMapping("/sort")
	public PostResponse getAllPostsSort(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
		return postService.getAllPostsSort(pageNo, pageSize);
	}

	@Operation(
			summary = "Get All Post REST API",
			description = "Get All Post REST API is used to fecth all posts from the database in sorted order either title, id etc"
			)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 OK"
			)
	// sort by id,title etc
	@GetMapping("/sortby")
	public PostResponse SortBy(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy) {
		return postService.sortBy(pageNo, pageSize, sortBy);
	}

	@Operation(
			summary = "Get All Post REST API",
			description = "Get All Post REST API is used to fecth all posts from the database in sorted order either ascending or descending"
			)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 OK"
			)
	// sort by id,title etc
	@GetMapping("/sortdir")
	public PostResponse SortByDir(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		return postService.sortByDir(pageNo, pageSize, sortBy, sortDir);
	}

	
	@Operation(
			summary = "Get Post REST API",
			description = "Get Post REST API is used to fetch a post from the database"
			)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status 201 CREATED"
			)
	// Get post by id
	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostbyId(@PathVariable(name = "id") long id) {
		return ResponseEntity.ok(postService.getPostById(id));

	}

	@SecurityRequirement(name = "Bearer Authentication")
	// update post
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id) {
		PostDto postResponse = postService.updatePost(postDto, id);
		return new ResponseEntity<>(postResponse, HttpStatus.OK);
	}
	

	@SecurityRequirement(name = "Bearer Authentication")
	// delete a post
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
		postService.deletePost(id);
		return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
	}
	
	// Build Get Post by Category REST API
	@GetMapping("/category/{id}")
	public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable(name="id") Long id){
		List<PostDto> postDtos = postService.getPostByCategory(id);
		return ResponseEntity.ok(postDtos);
	}

}
