package com.demo.gram.service;

import com.demo.gram.entity.Post;


import java.util.List;
import java.util.Optional;

public interface PostService {
  List<Post> findAllPosts();
  Optional<Post> findPostById(Long id);
}
