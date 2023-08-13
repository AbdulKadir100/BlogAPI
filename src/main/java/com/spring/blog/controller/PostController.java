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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	// private PostServiceImpl postServiceImpl;
	// we are using here interface instead of class, loose coupled via interface
	private PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	// create post rest API for blog
	// only admin can access this method
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
		return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
	}

	// Get all posts rest api and applying pagination and sorting
	@GetMapping
	public List<PostDto> getAllPosts(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
		return postService.getAllPosts(pageNo, pageSize);
	}

	// Get all posts rest api and applying sorting
	@GetMapping("/sort")
	public PostResponse getAllPostsSort(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
		return postService.getAllPostsSort(pageNo, pageSize);
	}

	// sort by id,title etc
	@GetMapping("/sortby")
	public PostResponse SortBy(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy) {
		return postService.sortBy(pageNo, pageSize, sortBy);
	}

	// sort by id,title etc
	@GetMapping("/sortdir")
	public PostResponse SortByDir(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		return postService.sortByDir(pageNo, pageSize, sortBy, sortDir);
	}

	// Get post by id
	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostbyId(@PathVariable(name = "id") long id) {
		return ResponseEntity.ok(postService.getPostById(id));

	}

	// update post
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id) {
		PostDto postResponse = postService.updatePost(postDto, id);
		return new ResponseEntity<>(postResponse, HttpStatus.OK);
	}

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
