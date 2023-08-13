package com.spring.blog.service;

import java.util.List;

import com.spring.blog.payload.CommentDto;

public interface CommentService {
	
	CommentDto createComment(long id, CommentDto commentDto);
	
	List<CommentDto> getCommentsById(long postId);
	
	CommentDto getCommentById(Long postId, Long commentId);
	
	CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest);
	
	void deleteCommenet(Long postId, Long commentId);
	

}
