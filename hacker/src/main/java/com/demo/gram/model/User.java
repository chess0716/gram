package com.demo.gram.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String email;

  // 연관 관계 - 사용자가 작성한 여러 채팅 메시지
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ChatMessage> messages = new HashSet<>();

  // 연관 관계 - 사용자가 작성한 게시글
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Post> posts = new HashSet<>();

  // 기본 생성자
  public User() {
  }

  // 사용자 이름, 비밀번호, 이메일을 받는 생성자
  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }
}