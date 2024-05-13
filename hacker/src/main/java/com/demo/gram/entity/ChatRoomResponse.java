package com.demo.gram.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {
  private Long chatRoomId;
  private Long postId;
}
