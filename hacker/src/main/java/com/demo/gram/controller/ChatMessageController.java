package com.demo.gram.controller;

import com.demo.gram.entity.ChatMessage;
import com.demo.gram.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/messages")
@RequiredArgsConstructor
public class ChatMessageController {
  private final ChatMessageRepository chatMessageRepository;



    @GetMapping("/{chatRoomId}")
  public ResponseEntity<List<ChatMessage>> getMessagesByChatRoomId(@PathVariable Long chatRoomId) {
    List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
    return ResponseEntity.ok(messages);
  }
}
