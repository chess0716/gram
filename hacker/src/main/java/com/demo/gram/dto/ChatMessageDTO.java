package com.demo.gram.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatMessageDTO {

  private Long id;
  private String message;
  private LocalDateTime timestamp;
  private Long chatRoomId;
  private Long senderId;
  private String senderName;

  @JsonCreator
  public ChatMessageDTO(
      @JsonProperty("id") Long id,
      @JsonProperty("message") String message,
      @JsonProperty("timestamp") LocalDateTime timestamp,
      @JsonProperty("chatRoomId") Long chatRoomId,
      @JsonProperty("senderId") Long senderId,
      @JsonProperty("senderName") String senderName) {
    this.id = id;
    this.message = message;
    this.timestamp = timestamp;
    this.chatRoomId = chatRoomId;
    this.senderId = senderId;
    this.senderName = senderName;
  }
}
