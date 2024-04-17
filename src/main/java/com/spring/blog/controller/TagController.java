package com.spring.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.blog.payload.TagDto;
import com.spring.blog.service.TagService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tags/posts")
public class TagController {

    private final TagService tagService;

    // Constructor-based dependency injection
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Create a tag associated with a specific post ID.
     * @param postId the ID of the post
     * @param tagDto the tag data transfer object
     * @return ResponseEntity with the created tag and HTTP status CREATED
     */
    @PostMapping("/{postId}")
    public ResponseEntity<TagDto> createTag(@PathVariable(value = "postId") long postId, @RequestBody @Valid TagDto tagDto) {
        // Create the tag and return a ResponseEntity with HTTP status CREATED
        TagDto createdTag = tagService.createTag(postId, tagDto);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }
}