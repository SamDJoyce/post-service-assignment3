package org.ac.cst8277.Joyce.Samuel.post_service;

import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/post")
public class PostController {

	private final PostService service;
	
	public PostController(PostService service) {
		this.service = service;
	}
	
	// Create New Post
	@PostMapping
	public Mono<Boolean> newPost(@RequestParam String token,
								 @RequestParam String content){
		// Verify token and get user_id which 
		// producer_id = user_id
		return service.newPost(token, content);
	}
	
	// Get all posts
	@GetMapping("/all")
	public Flux<Post> getAllPosts(@RequestParam String token){
		return service.getAllPosts(token);
	}
	
	// Get all posts from a single user
	@GetMapping("/from-user")
	public Flux<Post> getAllPostsByProducer(
			@RequestParam String token,
			@RequestParam Integer producer_id){
		return service.getAllByProducer(token, producer_id);
	}
}
