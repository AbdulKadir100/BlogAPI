package com.spring.blog.service;

import java.util.List;

import com.spring.blog.payload.TagDto;

public interface TagService {
	TagDto createTag(long id,TagDto tagDto);
	List<TagDto> getTags(long postId);

}
