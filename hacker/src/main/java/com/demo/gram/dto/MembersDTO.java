package com.demo.gram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembersDTO {

  private Long mno; // 유저 번호 (자동 증가)
  private String id; // 유저 아이디
  private String password; // 유저 비밀번호
  private String name; // 유저 이름
  private String email; // 유저 이메일
  private String mobile; // 유저 번호
  private boolean fromSocial; // 소셜로그인 및 일반로그인 구분
  private LocalDateTime regDate; // 회원가입일
  private LocalDateTime modDate; // 사용자 정보 최종 수정시간
  @Builder.Default
  private Set<String> roleSet = new HashSet<>();


  public MembersDTO(String id, String name) {
    this.id = id;
    this.name = name;
  }
}
