package org.ac.cst8277.Joyce.Samuel.post_service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class PostService {
	private final PostRepository repo;
	private WebClient webC = WebClient.create("http://localhost:8080");
	
	public PostService(PostRepository repo) {
		this.repo = repo;
	}
	
	public Mono<Boolean> newPost(String token, String content) {
	    return validateToken(token)
	        .flatMap(userId -> {
	            if (userId == null || userId == 0) {
	                return Mono.just(false);
	            }

	            return Mono.fromCallable(() -> {
	                Post newPost = new Post(userId, content);
	                Post saved = repo.save(newPost);
	                return saved != null;
	            }).subscribeOn(Schedulers.boundedElastic());
	        });
	}
	
	public Flux<Post> getAllPosts(String token) {
	    return validateToken(token)
	        .flatMapMany(userId -> {
	            if (userId == null || userId == 0) {
	                return Flux.empty();
	            }

	            return Mono.fromCallable(repo::findAll)
	                    .subscribeOn(Schedulers.boundedElastic())
	                    .flatMapMany(Flux::fromIterable);
	        });
	}
	
	public Flux<Post> getAllByProducer(String token, Integer producerId) {
	    return validateToken(token)
	        .flatMapMany(userId -> {
	            if (userId == null || userId == 0) {
	                return Flux.empty();
	            }

	            return Mono.fromCallable(() -> repo.findByProducerId(producerId))
	                    .subscribeOn(Schedulers.boundedElastic())
	                    .flatMapMany(Flux::fromIterable);
	        });
	}
	
	private Mono<Integer> validateToken(String token) {
	    return webC.get()
	        .uri(uriBuilder -> uriBuilder
	                .path("/users/validate")
	                .queryParam("token", token)
	                .build())
	        .retrieve()
	        .bodyToMono(Integer.class);
	}

}
