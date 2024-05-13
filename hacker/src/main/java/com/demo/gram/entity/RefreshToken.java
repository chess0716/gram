package com.demo.gram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  // 사용자는 userId로 식별되며, 각 사용자마다 하나의 새로 고침 토큰
  @Column(nullable = false, unique = true)
  private Long userId;

  @Column(nullable = false)
  private String refreshToken;

  // 새로운 새로 고침 토큰으로 토큰을 업데이트하기 위해 사용
  public RefreshToken update(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }
}
