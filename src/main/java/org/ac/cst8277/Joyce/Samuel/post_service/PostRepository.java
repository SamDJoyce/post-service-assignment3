package org.ac.cst8277.Joyce.Samuel.post_service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer>{
	List<Post> findByProducerId(Integer producerId);
}
	

