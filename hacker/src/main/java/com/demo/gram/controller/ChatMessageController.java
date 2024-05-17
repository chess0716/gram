package com.demo.gram.controller;

import com.demo.gram.dto.ChatMessageDTO;
import com.demo.gram.entity.ChatMessage;
import com.demo.gram.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat/messages")
@RequiredArgsConstructor
@Log4j2
public class ChatMessageController {
  private final ChatMessageRepository chatMessageRepository;

  @GetMapping("/{chatRoomId}")
  public ResponseEntity<List<ChatMessageDTO>> getMessagesByChatRoomId(@PathVariable Long chatRoomId) {
    log.info("Fetching messages for chatRoomId: {}", chatRoomId);

    List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
    log.info("Messages found: {}", messages.size());

    List<ChatMessageDTO> messageDTOs = messages.stream()
        .map(msg -> new ChatMessageDTO(msg.getId(), msg.getMessage(), msg.getTimestamp(), msg.getChatRoom().getId(), msg.getSender().getMno(), msg.getSender().getName()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(messageDTOs);
  }
}
