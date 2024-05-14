package com.demo.gram.repository;

import com.demo.gram.entity.Members;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, Long> {

  @Query("select m from Members m")
  List<Members> getAll();

  @Query("SELECT m FROM Members m WHERE m.email = :email")
  Optional<Members> findByEmail(@Param("email") String email);

  @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
  @Query("select m from Members m where m.fromSocial = :social and m.id = :id")
  Optional<Members> findById(@Param("id") String id, @Param("social") boolean social);

  @Query("select m from Members m join m.chatRooms c where c.id = :chatRoomId")
  List<Members> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
