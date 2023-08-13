package com.spring.blog.service;

import java.util.List;

import com.spring.blog.payload.CategoryDTO;

public interface CategoryService {

	CategoryDTO addCategory(CategoryDTO categoryDTO);
	CategoryDTO getCategory(long categoryId);
	List<CategoryDTO> getAllCategories();
	CategoryDTO updateCategory(long id, CategoryDTO categoryDTO);
	void deleteCategory(long id);

}
