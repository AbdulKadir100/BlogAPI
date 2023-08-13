package com.spring.blog.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
		name="posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
		)
public class Post {
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
	private Long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "content", nullable = false) 
	private String content;
	
	// one post have many comments
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) 
	private Set<Comment> comments = new HashSet<>();
	
	
	// Enabling ManyToOne mapping b/w Post and Category
	// Many Post have one category
	// Category table is a parent table and Post is child
	// when define many to one then foreign key column will be created in the child(Post) table
	@ManyToOne(fetch = FetchType.LAZY) //when we load post its category not load immediately
	@JoinColumn(name = "category_id")
	private Category category;

}
