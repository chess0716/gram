package com.demo.gram.controller;

import com.demo.gram.entity.Post;
import com.demo.gram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping
  @CrossOrigin(origins = "http://localhost:3000")
  public ResponseEntity<List<Post>> getAllPosts() {
    List<Post> posts = postService.findAllPosts();
    return ResponseEntity.ok(posts);
  }

  @GetMapping("/{id}")
  @CrossOrigin(origins = "http://localhost:3000")
  public ResponseEntity<Post> getPostById(@PathVariable Long id) {
    Optional<Post> post = postService.findPostById(id);
    return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

}