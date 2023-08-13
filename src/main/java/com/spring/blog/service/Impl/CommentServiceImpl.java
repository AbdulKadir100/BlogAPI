package com.spring.blog.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.spring.blog.entity.Comment;
import com.spring.blog.entity.Post;
import com.spring.blog.exception.BlogAPIException;
import com.spring.blog.exception.ResourceNotFoundException;
import com.spring.blog.payload.CommentDto;
import com.spring.blog.repository.CommentRepository;
import com.spring.blog.repository.PostRepository;
import com.spring.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	private CommentRepository commentRepository;
	private PostRepository postRepository;
	private ModelMapper mapper;

	// Note : We do not need to auto wired this class bcoz there is only one
	// constructor
	// spring bean auto handle it as auto wired
	public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.mapper = mapper;
	}

	@Override
	public CommentDto createComment(long id, CommentDto commentDto) {

		Comment comment = mapToEntity(commentDto);

		// retrieve post by id
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		// set post to comment entity
		comment.setPost(post);

		// comment entity to DB
		Comment newcomment = commentRepository.save(comment);

		return mapToDTO(newcomment);
	}

	// retrieve comments by postId
	@Override
	public List<CommentDto> getCommentsById(long postId) {
		List<Comment> comments = commentRepository.findByPostId(postId);

		// convert list of comments to commentDTO
		return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
	}

	// retrieve by post id and comment id
	@Override
	public CommentDto getCommentById(Long postId, Long commentId) {

		// retrieve post by id
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		// retrieve comment by id
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

		// check if comment belongs to post or not
		if (!comment.getPost().getId().equals(post.getId())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
		}

		return mapToDTO(comment);
	}

	// update the comment
	@Override
	public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {

		// retrieve post by id, if it is not present in the DB just throw exception
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		// retrieve comment by id
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

		// check if comment belongs to post or not
		if (!comment.getPost().getId().equals(post.getId())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
		}

		// set up the data to comment
		comment.setName(commentRequest.getName());
		comment.setEmail(commentRequest.getEmail());
		comment.setBody(commentRequest.getBody());

		// save and map to DTO , response to client
		Comment updatedComment = commentRepository.save(comment);
		return mapToDTO(updatedComment);

	}

	// delete a comment
	@Override
	public void deleteCommenet(Long postId, Long commentId) {
		// retrieve post by id, if it is not present in the DB just throw exception
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		// retrieve comment by id
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

		// check if comment belongs to post or not
		if (!comment.getPost().getId().equals(post.getId())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
		}
		
		commentRepository.delete(comment);
	}

	private CommentDto mapToDTO(Comment comment) {
		CommentDto commentDto = mapper.map(comment, CommentDto.class);
//		CommentDto commentDto = new CommentDto();
//		commentDto.setId(comment.getId());
//		commentDto.setName(comment.getName());
//		commentDto.setEmail(comment.getEmail());
//		commentDto.setBody(comment.getBody());
		return commentDto;
	}

	private Comment mapToEntity(CommentDto commentDto) {
		Comment comment = mapper.map(commentDto, Comment.class);
//		Comment comment = new Comment();
//		comment.setId(commentDto.getId());
//		comment.setName(commentDto.getName());
//		comment.setEmail(commentDto.getEmail());
//		comment.setBody(commentDto.getBody());
		return comment;
	}

}
