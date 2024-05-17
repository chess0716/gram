package com.demo.gram.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Members {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;

  @Column(unique = true) // 중복 방지
  private String id;

  @Column(nullable = false)
  private String password;

  @Column(unique = true)
  private String email;
  private String name;
  private String mobile;
  private boolean fromSocial;
  private LocalDateTime regDate;
  private LocalDateTime modDate;

  @ElementCollection(fetch = FetchType.LAZY)
  @Builder.Default
  private Set<MembersRole> roleSet = new HashSet<>();

  @ManyToMany(mappedBy = "members", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<ChatRoom> chatRooms = new HashSet<>();

  public void addMemberRole(MembersRole membersRole) {
    roleSet.add(membersRole);
  }

  public void joinChatRoom(ChatRoom chatRoom) {
    this.chatRooms.add(chatRoom);
    chatRoom.getMembers().add(this); // ChatRoom 엔티티에도 반대 방향의 관계 설정
  }
}
