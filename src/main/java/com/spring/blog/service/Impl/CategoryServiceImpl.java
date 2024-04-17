package com.spring.blog.service.Impl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.blog.entity.Category;
import com.spring.blog.exception.ResourceNotFoundException;
import com.spring.blog.payload.CategoryDTO;
import com.spring.blog.repository.CategoryRepository;
import com.spring.blog.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	private CategoryRepository categoryRepository;
	private ModelMapper modelMapper;
	
	@Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
	}



	@Override
	public CategoryDTO addCategory(CategoryDTO categoryDTO) {
		Category category = modelMapper.map(categoryDTO, Category.class);
		Category savedCategory = categoryRepository.save(category);
		
		return modelMapper.map(savedCategory, CategoryDTO.class);
	}



	@Override
	public List<CategoryDTO> getAllCategories() {
		
		List<Category> catList = categoryRepository.findAll();
		
		return catList.stream().map((category) -> modelMapper.map(category, CategoryDTO.class))
				.collect(Collectors.toList());
				
		
//		return categoryRepository.findAll().stream()
//				.map(category -> modelMapper.map(category, CategoryDTO.class))
//				.collect(Collectors.toList());
	}



	@Override
	public CategoryDTO updateCategory(long id, CategoryDTO categoryDTO) {
		
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category ", "id :", id));
		
		category.setName(categoryDTO.getName());
		category.setDescription(categoryDTO.getDescription());
		//category.setId(categoryDTO.getId());
		
		Category updatedCategory = categoryRepository.save(category);
		
		CategoryDTO dto = modelMapper.map(updatedCategory, CategoryDTO.class);
		
		return dto;
	}



	@Override
	public void deleteCategory(long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id ", id));
		categoryRepository.delete(category);
	}



	@Override
	public CategoryDTO getCategory(long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id ", categoryId));
		return modelMapper.map(category, CategoryDTO.class);
	}

}
