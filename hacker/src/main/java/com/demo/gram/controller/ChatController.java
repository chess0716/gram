package com.demo.gram.controller;

import com.demo.gram.dto.ChatMessageDTO;
import com.demo.gram.dto.MembersDTO;
import com.demo.gram.entity.ChatMessage;
import com.demo.gram.entity.ChatRoom;
import com.demo.gram.entity.ChatRoomResponse;
import com.demo.gram.repository.ChatMessageRepository;
import com.demo.gram.repository.ChatRoomRepository;
import com.demo.gram.security.util.JWTUtil;
import com.demo.gram.service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Log4j2
public class ChatController {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final MembersService membersService;
  private final JWTUtil jwtUtil;

  @MessageMapping("/chat/{chatRoomId}/send")
  @SendTo("/topic/chat/{chatRoomId}")
  public ChatMessageDTO sendChatMessageViaWebSocket(@DestinationVariable Long chatRoomId, @Payload ChatMessage chatMessage) {
    log.info("WebSocket Message received for chatRoomId: {}", chatRoomId);
    log.info("Message content: {}", chatMessage.getMessage());

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> {
          log.error("Chat room not found for chatRoomId: {}", chatRoomId);
          return new RuntimeException("Chat room not found");
        });

    chatMessage.setChatRoom(chatRoom);
    chatMessageRepository.save(chatMessage);

    log.info("Message saved and broadcasted: {}", chatMessage.getMessage());

    return new ChatMessageDTO(chatMessage.getId(), chatMessage.getMessage(), chatMessage.getTimestamp(), chatRoom.getId(), chatMessage.getSenderName());
  }

  @PostMapping("/{chatRoomId}/send")
  public ResponseEntity<ChatMessageDTO> sendChatMessageViaPost(@PathVariable Long chatRoomId, @RequestBody ChatMessage chatMessage) {
    log.info("HTTP Message received for chatRoomId: {}", chatRoomId);
    log.info("Message content: {}", chatMessage.getMessage());

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> {
          log.error("Chat room not found for chatRoomId: {}", chatRoomId);
          return new RuntimeException("ChatRoom not found");
        });

    chatMessage.setChatRoom(chatRoom);
    chatMessageRepository.save(chatMessage);

    log.info("Message saved via HTTP: {}", chatMessage.getMessage());

    return ResponseEntity.ok(new ChatMessageDTO(chatMessage.getId(), chatMessage.getMessage(), chatMessage.getTimestamp(), chatRoom.getId(), chatMessage.getSenderName()));
  }

  @GetMapping("/room/by-post/{postId}")
  public ResponseEntity<ChatRoomResponse> getChatRoomByPostId(@PathVariable Long postId) {
    log.info("Fetching chat room for postId: {}", postId);

    ChatRoom chatRoom = chatRoomRepository.findByPostId(postId)
        .orElseThrow(() -> {
          log.error("No chat room associated with the provided post ID: {}", postId);
          return new RuntimeException("No chat room associated with the provided post ID");
        });

    ChatRoomResponse response = new ChatRoomResponse(chatRoom.getId(), postId);
    log.info("Chat room found for postId: {}", postId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/chatroom/{chatRoomId}/members")
  public ResponseEntity<List<MembersDTO>> getChatRoomMembers(@PathVariable Long chatRoomId) {
    log.info("Getting members for chat room: {}", chatRoomId);

    List<MembersDTO> members = membersService.getChatRoomMembers(chatRoomId);
    log.info("Members found: {}", members.size());
    return new ResponseEntity<>(members, HttpStatus.OK);
  }

  @Transactional
  @MessageMapping("/chat/{chatRoomId}/join")
  @SendTo("/topic/chat/{chatRoomId}/members")
  public List<MembersDTO> joinChatRoom(@DestinationVariable Long chatRoomId, @Payload String token) {
    log.info("Join request received for chatRoomId: {}", chatRoomId);
    try {
      String email = jwtUtil.validateAndExtract(token);
      log.info("Token validated for email: {}", email);

      membersService.joinChatRoom(email, chatRoomId);
    } catch (Exception e) {
      log.error("Token validation failed", e);
      throw new RuntimeException("Invalid token", e);
    }

    List<MembersDTO> members = membersService.getChatRoomMembers(chatRoomId);
    log.info("Members list sent for chatRoomId: {}", chatRoomId);
    return members;
  }
}
