package com.spring.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.blog.payload.CommentDto;
import com.spring.blog.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/")
public class CommentController {

	private CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	// rest API for creating comments
	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<CommentDto> createComment( @PathVariable(value = "postId") long postId,
			@Valid @RequestBody CommentDto commentDto) {

		return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
	}

	// rest API for get all comments to a post
	@GetMapping("/posts/{postId}/comments")
	public List<CommentDto> getAllCommentsById(@PathVariable(value = "postId") long postId) {
		return commentService.getCommentsById(postId);

	}

	// rest API to get single comment
	@GetMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") Long postId,
			@PathVariable(value = "id") Long commentId) {
		return new ResponseEntity<>(commentService.getCommentById(postId, commentId), HttpStatus.OK);

	}

	// rest API to update the comment
	@PutMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<CommentDto> updateComment( @PathVariable(value = "postId") Long postId,
			@PathVariable(value = "id") Long commentId,@Valid @RequestBody CommentDto commentRequest) {

		CommentDto updatedComment = commentService.updateComment(postId, commentId, commentRequest);
		return new ResponseEntity<>(updatedComment, HttpStatus.OK);

	}

	// rest API to delete a comment
	@DeleteMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
			@PathVariable(value = "id") Long commentId) {
		commentService.deleteCommenet(postId, commentId);
		return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);

	}

}
