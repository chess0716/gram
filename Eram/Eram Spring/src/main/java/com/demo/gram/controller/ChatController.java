package com.demo.gram.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
  public ChatMessage sendChatMessageViaWebSocket(@DestinationVariable Long chatRoomId, ChatMessage chatMessage) {
    chatMessage.setChatRoom(chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new RuntimeException("Chat room not found")));
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
  public ChatMessage sendChatMessageForPost(@DestinationVariable Long postId, ChatMessage chatMessage) {
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

  @GetMapping("/chatroom/{chatRoomId}/user")
  public ResponseEntity<List<MembersDTO>> getChatRoomMembers(@PathVariable Long chatRoomId) {
    log.info("Getting members for chat room: " + chatRoomId);
    List<MembersDTO> members = membersService.getChatRoomMembers(chatRoomId);
    return new ResponseEntity<>(members, HttpStatus.OK);
  }

  @MessageMapping("/chat/{chatRoomId}/join")
  @SendTo("/topic/chat/{chatRoomId}/members")
  public List<MembersDTO> joinChatRoom(@DestinationVariable Long chatRoomId, @Payload String token) {
    try {
      String email = jwtUtil.validateAndExtract(token);
      membersService.joinChatRoom(email, chatRoomId);
    } catch (Exception e) {
      log.error("Token validation failed", e);
      throw new RuntimeException("Invalid token", e);
    }
    return membersService.getChatRoomMembers(chatRoomId);
  }
}
