package com.demo.gram.repository;

import com.demo.gram.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  Optional<ChatRoom> findByPostId(Long postId);
  List<ChatRoom> findByMembers_Mno(Long mno);
  // 특정 사용자가 참여한 채팅방을 가져오는 메서드

}
