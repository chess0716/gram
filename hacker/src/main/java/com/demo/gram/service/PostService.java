package com.demo.gram.service;

import com.demo.gram.model.Post;


import java.util.List;
import java.util.Optional;

public interface PostService {
  List<Post> findAllPosts();
  Optional<Post> findPostById(Long id);
}
