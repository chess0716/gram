package com.demo.gram.controller;

import com.demo.gram.model.ChatMessage;
import com.demo.gram.model.ChatRoom;
import com.demo.gram.model.ChatRoomResponse;
import com.demo.gram.repository.ChatMessageRepository;
import com.demo.gram.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;

  @MessageMapping("/chat/{chatRoomId}/send")
  @SendTo("/topic/chat/{chatRoomId}")
  public ChatMessage sendChatMessageViaWebSocket(@DestinationVariable Long chatRoomId, ChatMessage chatMessage) {
    chatMessage.setChatRoom(chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("Chat room not found")));
    chatMessageRepository.save(chatMessage);
    return chatMessage;
  }

  @PostMapping("/{chatRoomId}/send")
  public ResponseEntity<ChatMessage> sendChatMessageViaPost(@PathVariable Long chatRoomId, @RequestBody ChatMessage chatMessage) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
    chatMessage.setChatRoom(chatRoom);
    chatMessageRepository.save(chatMessage);
    return ResponseEntity.ok(chatMessage);
  }

  @MessageMapping("/message/{postId}")
  @SendTo("/topic/messages/{postId}")
  public ChatMessage sendChatMessageForPost(@PathVariable Long postId, ChatMessage chatMessage) {
    ChatRoom chatRoom = chatRoomRepository.findByPostId(postId)
            .orElseThrow(() -> new RuntimeException("No chat room associated with the provided post ID"));
    chatMessage.setChatRoom(chatRoom);
    chatMessageRepository.save(chatMessage);
    return chatMessage;
  }

  @GetMapping("/room/by-post/{postId}")
  public ResponseEntity<ChatRoomResponse> getChatRoomByPostId(@PathVariable Long postId) {
    ChatRoom chatRoom = chatRoomRepository.findByPostId(postId)
            .orElseThrow(() -> new RuntimeException("No chat room associated with the provided post ID"));
    ChatRoomResponse response = new ChatRoomResponse(chatRoom.getId(), postId);
    return ResponseEntity.ok(response);
  }
}
