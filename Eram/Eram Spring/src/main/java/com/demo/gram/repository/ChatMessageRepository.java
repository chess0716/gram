package com.demo.gram.repository;



import com.demo.gram.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  // 특정 채팅방의 메시지를 조회하기 위한 메소드
  List<ChatMessage> findByChatRoomId(Long chatRoomId);
}
