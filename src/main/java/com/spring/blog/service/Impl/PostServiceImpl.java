package com.spring.blog.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.blog.entity.Category;
import com.spring.blog.entity.Post;
import com.spring.blog.exception.ResourceNotFoundException;
import com.spring.blog.payload.PostDto;
import com.spring.blog.payload.PostResponse;
import com.spring.blog.repository.CategoryRepository;
import com.spring.blog.repository.PostRepository;
import com.spring.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	private CategoryRepository categoryRepository;

	// inject model mapper, to map DTO into Entity and vice versa.
	private ModelMapper mapper;

	// @Autowired latest spring 3.0 version if there is only single bean
	// constructor we can omit this
	public PostServiceImpl(PostRepository postRepository, ModelMapper mapper, CategoryRepository categoryRepository) {
		this.postRepository = postRepository;
		this.mapper = mapper;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public PostDto createPost(PostDto postDto) {

		Category category = categoryRepository.findById(postDto.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id ", postDto.getCategoryId()));

		// convert Data Transfer Object(DTO) to Entity.
		Post post = mapToEntity(postDto);

		post.setCategory(category);

		// save into database as entity.
		Post newPost = postRepository.save(post);

		// convert entity to DTO to send back to client.
		PostDto postResponse = mapToDTO(newPost);
		return postResponse;
	}

	@Override
	public List<PostDto> getPostByCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

		List<Post> posts = postRepository.findByCategoryId(categoryId);

		return posts.stream().map((post) -> mapToDTO(post)).collect(Collectors.toList());

	}

	@Override
	public List<PostDto> getListOfPosts() {
		List<Post> posts = postRepository.findAll();
		return posts.stream().map(this :: mapToDTO).collect(Collectors.toList());
	}

	@Override
	public List<PostDto> getAllPosts(int pageNo, int pageSize) {

		// implementing paging

		// create page able instance with page no and page size
		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		// get all the page list from database.
		Page<Post> postList = postRepository.findAll(pageable);

		// get content for page object
		List<Post> listofPost = postList.getContent();

		// send list of post as DTO response
		return listofPost.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
	}

	@Override
	public PostResponse getAllPostsSort(int pageNo, int pageSize) {

		// implementing paging

		// create page able instance with page no and size
		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		// get all post list as pages
		Page<Post> postList = postRepository.findAll(pageable);

		// get content for page object (all posts from database)
		List<Post> listofPost = postList.getContent();

		// convert list of post into DTO (list of DTO)
		List<PostDto> content = listofPost.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

		// below code will show all the content of all the posts in database.
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(postList.getNumber());
		postResponse.setPageSize(postList.getSize());
		postResponse.setTotalElements(postList.getTotalElements());
		postResponse.setTotalPages(postList.getTotalPages());
		postResponse.setLast(postList.isLast());
		return postResponse;
	}

	// sort rest API for default id and other title,description etc.
	@Override
	public PostResponse sortBy(int pageNo, int pageSize, String sortBy) {
		// implementing paging

		// create page able instance and applying sorting based on string parameter.
		// by default it is in ascending order
		PageRequest pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		/*
		 * to get in descending order. PageRequest page able = PageRequest.of(pageNo,
		 * pageSize,Sort.by(sortBy).descending());
		 * 
		 */

		Page<Post> postList = postRepository.findAll(pageable);

		// get content for page object
		List<Post> listofPost = postList.getContent();

		List<PostDto> content = listofPost.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

		// below code will show all the content and all the posts
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(postList.getNumber());
		postResponse.setPageSize(postList.getSize());
		postResponse.setTotalElements(postList.getTotalElements());
		postResponse.setTotalPages(postList.getTotalPages());
		postResponse.setLast(postList.isLast());
		return postResponse;
	}

	// Sort by ascending or descending order
	@Override
	public PostResponse sortByDir(int pageNo, int pageSize, String sortBy, String sortDir) {

		// if sort object is ascending order we create object in ascending order else
		// descending order

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// create page able instance and applying sorting based on string parameter.
		PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);

		// create page able instance and applying sorting based on string parameter in
		// descending order.
		// PageRequest page able = PageRequest.of(pageNo, pageSize,
		// Sort.by(sortBy).descending());

		Page<Post> postList = postRepository.findAll(pageable);

		// get content for page object
		List<Post> listofPost = postList.getContent();

		// convert list of post into DTO (list of DTO)
		List<PostDto> content = listofPost.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

		// below code will show all the content and all the posts
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(postList.getNumber());
		postResponse.setPageSize(postList.getSize());
		postResponse.setTotalElements(postList.getTotalElements());
		postResponse.setTotalPages(postList.getTotalPages());
		postResponse.setLast(postList.isLast());
		return postResponse;
	}

	// post by id
	@Override
	public PostDto getPostById(long id) {

		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		return mapToDTO(post);

	}

	@Override
	public PostDto updatePost(PostDto postDto, long id) {
		// get post by id from database
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		Category category = categoryRepository.findById(postDto.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getId()));

		// updating data
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		post.setCategory(category);

		Post updatedPost = postRepository.save(post);

		// send back as DTO as response
		return mapToDTO(updatedPost);
	}

	@Override
	public void deletePost(long id) {
		postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		postRepository.deleteById(id);
	}

	// convert Entity into DTO
	private PostDto mapToDTO(Post post) {
		PostDto postDto = mapper.map(post, PostDto.class);
//			PostDto postDto = new PostDto();
//			postDto.setId(post.getId());
//			postDto.setTitle(post.getTitle());
//			postDto.setDescription(post.getDescription());
//			postDto.setContent(post.getContent());

		return postDto;

	}

	// convert DTO to Entity
	private Post mapToEntity(PostDto postDto) {
		Post post = mapper.map(postDto, Post.class);
//			Post post = new Post();
//			post.setTitle(postDto.getTitle());
//			post.setDescription(postDto.getDescription());
//			post.setContent(postDto.getContent());
		return post;
	}

}
