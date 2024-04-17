package com.spring.blog.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String email;
	private String body;

	// fetch type tells hibernate to only fetch the related entities
	// from the database when you use the relationship.
	@ManyToOne(fetch = FetchType.LAZY)
 
	// Join column is used for specify foreign key in comments table
	@JoinColumn(name = "post_id", nullable = false)
	private Post post; // specify this in Post class

}
