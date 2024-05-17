package com.demo.gram.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MyPostEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pno; // 페이지 번호 (자동증가)

  @Column(unique = true) // 중복 방지
  private String id; // 작성 아이디
  private String title; // 글 제목

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mno") // 멤버 번호를 외래 키로 사용
  private Members member;
}
