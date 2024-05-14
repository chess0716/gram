package com.demo.gram.security.service;


import com.demo.gram.entity.Members;
import com.demo.gram.repository.MembersRepository;
import com.demo.gram.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

// 사용자의 인증 정보를 데이터베이스에서 가져와 Spring Security에 제공
@Log4j2
@Service
@RequiredArgsConstructor
public class MembersUserDetailService implements UserDetailsService {

  private final MembersRepository membersRepository;

  // 사용자 이름(여기서는 이메일)을 받아와서 해당 사용자의 인증 정보를 로드
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("MembersUserDetailsService loadUserByUsername: " + username);
    Optional<Members> result = membersRepository.findByEmail(username);

    // 사용자가 데이터베이스에 없으면 Exception 발생
    if (!result.isPresent()) throw new UsernameNotFoundException("Check Email or Social");

    Members members = result.get();
    AuthMemberDTO authMemberDTO = new AuthMemberDTO(
        members.getEmail(),
        members.getMno(),
        members.getPassword(),
        members.isFromSocial(),
        members.getRoleSet().stream().map(
            role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList())
    );
    authMemberDTO.setName(members.getName());
    authMemberDTO.setFromSocial(members.isFromSocial());
    log.info(">>>" + authMemberDTO);
    log.info("authMemberDTO.getAuthorities: " + authMemberDTO.getAttributes());
    return authMemberDTO;
  }
}
