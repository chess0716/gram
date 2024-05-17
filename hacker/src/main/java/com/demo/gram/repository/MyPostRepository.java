package com.demo.gram.repository;


import com.demo.gram.entity.MyPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPostRepository extends JpaRepository<MyPostEntity, Long> {

  // 특정 사용자가 작성한 게시물을 가져오는 메서드
  List<MyPostEntity> findByMember_Mno(Long mno);

}
