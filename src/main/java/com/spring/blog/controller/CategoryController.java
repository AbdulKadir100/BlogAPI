package com.spring.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.blog.payload.CategoryDTO;
import com.spring.blog.service.CategoryService;

@RestController
@RequestMapping("/api/categories/")
public class CategoryController {
	
	private CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	// Build add category REST API
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
		CategoryDTO savedCategory  = categoryService.addCategory(categoryDTO);
		return new ResponseEntity<>(savedCategory,HttpStatus.CREATED);
	}
	
	// Build Get Category REST API
	@GetMapping("{id}")
	public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") long categoryId){
		CategoryDTO categoryDTO = categoryService.getCategory(categoryId);
		return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
	}
	
	// Build Get All Categories REST API
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> getAllCategories(){
		return ResponseEntity.ok(categoryService.getAllCategories());
	}
	
	// Build Update REST API
	@PutMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("id") long categoryId,@RequestBody CategoryDTO categoryDTO){
		return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDTO));
	}

	// Build delete REST API
	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteCategory(@PathVariable("id") long categoryId) {
		categoryService.deleteCategory(categoryId);
		return ResponseEntity.ok("Category deleted successfully!.");
	}
}
