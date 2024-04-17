package com.spring.blog.service.Impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.spring.blog.entity.Comment;
import com.spring.blog.entity.Post;
import com.spring.blog.entity.Tag;
import com.spring.blog.exception.ResourceNotFoundException;
import com.spring.blog.payload.CommentDto;
import com.spring.blog.payload.TagDto;
import com.spring.blog.repository.PostRepository;
import com.spring.blog.repository.TagRepository;
import com.spring.blog.service.TagService;

import lombok.RequiredArgsConstructor;

@Service
public class TagServiceImpl implements TagService {
	private TagRepository tagRepository;
	private PostRepository postRepository;
	private ModelMapper mapper;

	public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository, ModelMapper mapper) {
		this.tagRepository = tagRepository;
		this.postRepository = postRepository;
		this.mapper = mapper;
	}

//	public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository) {
//		this.tagRepository = tagRepository;
//		this.postRepository = postRepository;
//	}

	@Override
	public TagDto createTag(long id, TagDto tagDto) {
		if (tagDto == null) {
			throw new IllegalArgumentException("Tag data cannot be null");
		}
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		Tag tag = mapToEntity(tagDto);

		// Associate the tag with the found post
		tag.setPost(post);
		tag = tagRepository.save(tag);
		return mapToDTO(tag);
	}

	@Override
	public List<TagDto> getTags(long postId) {
		List<Tag> listTags = tagRepository.findByPostId(postId);
		if (listTags.size() == 0) {
			throw new ResourceNotFoundException("0 posts are there..", "PostID", postId);
		}
		return listTags.stream().map(this::mapToDTO).collect(Collectors.toList());

	}

	private TagDto mapToDTO(Tag tag) {
		TagDto tagDto = mapper.map(tag, TagDto.class);
		return tagDto;
	}

	private Tag mapToEntity(TagDto tagDto) {
		Tag tag = mapper.map(tagDto, Tag.class);
		return tag;
	}
}
