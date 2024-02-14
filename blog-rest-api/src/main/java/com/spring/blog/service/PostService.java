package com.spring.blog.service;

import java.util.List;

import com.spring.blog.payload.PostDto;
import com.spring.blog.payload.PostResponse;

public interface PostService {

	// create post in database
	PostDto createPost(PostDto postDto);

	// get all post from database
	List<PostDto> getAllPosts(int pageNo, int pageSize);

	// get all post by page
	PostResponse getAllPostsSort(int pageNo, int pageSize);

	// get all in sorted order via id,title etc
	PostResponse sortBy(int pageNo, int pageSize, String sortBy);

	// get the sorted order ascending(asc) or descending(desc)
	PostResponse sortByDir(int pageNo, int pageSize, String sortBy, String sortDir);

	// get a post by id
	PostDto getPostById(long id);

	// update a post
	PostDto updatePost(PostDto postDto, long id);

	// delete a post
	void deletePost(long id);
	
    List<PostDto> getPostByCategory(Long category);	
}
