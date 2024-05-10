package com.demo.gram.model;


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
