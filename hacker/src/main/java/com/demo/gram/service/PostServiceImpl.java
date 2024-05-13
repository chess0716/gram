package com.demo.gram.service;


import com.demo.gram.entity.Post;
import com.demo.gram.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  @Autowired
  public PostServiceImpl(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public List<Post> findAllPosts() {
    return postRepository.findAll();
  }

  @Override
  public Optional<Post> findPostById(Long id) {
    return postRepository.findById(id);
  }
}
