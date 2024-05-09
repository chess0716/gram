package com.demo.gram.controller;

import com.demo.gram.model.ChatMessage;
import com.demo.gram.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatMessageController {

  @Autowired
  private ChatMessageRepository chatMessageRepository;

  @GetMapping("/chat/{roomId}/messages")
  public List<ChatMessage> getMessagesByRoomId(@PathVariable Long roomId) {
    return chatMessageRepository.findByChatRoomId(roomId);
  }
}
