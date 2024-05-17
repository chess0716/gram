package com.demo.gram.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chat_rooms")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "post"})
public class ChatRoom {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @ManyToMany
  @JoinTable(
      name = "member_chatrooms",
      joinColumns = @JoinColumn(name = "chatroom_id"),
      inverseJoinColumns = @JoinColumn(name = "member_id")
  )
  private Set<Members> members;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnoreProperties("chatRoom")
  private List<ChatMessage> messages = new ArrayList<>();
}
