package com.demo.gram.repository;

import com.demo.gram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {



}
