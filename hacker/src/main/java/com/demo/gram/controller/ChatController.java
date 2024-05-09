package com.demo.gram.controller;

import com.demo.gram.model.ChatMessage;
import com.demo.gram.model.ChatRoom;
import com.demo.gram.repository.ChatMessageRepository;
import com.demo.gram.repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  @MessageMapping("/chat.send")
  @SendTo("/topic/public")
  public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    // 메시지를 데이터베이스에 저장
    chatMessageRepository.save(chatMessage);
    return chatMessage;
  }

  @MessageMapping("/chat/{roomId}/send")
  @SendTo("/topic/chatroom/{roomId}")
  public ChatMessage sendChatMessage(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage) {
    // roomId를 기반으로 ChatRoom 객체를 찾아서 설정
    ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
    chatMessage.setChatRoom(chatRoom);
    chatMessageRepository.save(chatMessage);
    return chatMessage;
  }
}