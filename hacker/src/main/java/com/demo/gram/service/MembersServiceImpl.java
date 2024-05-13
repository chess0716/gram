package com.demo.gram.service;


import com.demo.gram.dto.MembersDTO;
import com.demo.gram.entity.Members;
import com.demo.gram.repository.MembersRepository;
import com.demo.gram.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService , UserDetailsService {

  private final MembersRepository membersRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Members user = membersRepository.findByEmail(email, false)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .roles("USER") // 사용자의 권한을 설정합니다.
        .build();
  }

  @Override
  public Long registMembersDTO(MembersDTO membersDTO) {
    membersDTO.setPassword(passwordEncoder.encode(membersDTO.getPassword()));
    return membersRepository.save(dtoToEntity(membersDTO)).getMno();
  }

  @Override
  public void updateMembersDTO(MembersDTO membersDTO) {
    membersRepository.save(dtoToEntity(membersDTO)).getMno();
  }

  @Override
  public void removeMembers(Long num) {
    membersRepository.deleteById(num);
  }

  @Override
  public MembersDTO get(Long num) {
    Optional<Members> result = membersRepository.findById(num);
    if (result.isPresent()) {
      return entityToDTO(result.get());
    }
    return null;
  }

  @Override
  public List<MembersDTO> getAll() {
    List<Members> membersList = membersRepository.getAll();
    return membersList.stream().map(
        new Function<Members, MembersDTO>() {
          @Override
          public MembersDTO apply(Members members) {
            return entityToDTO(members);
          }
        }
    ).collect(Collectors.toList());
  }

  @Override
  public String login(String email, String password, JWTUtil jwtUtil) {
    log.info("login............");
    String token = "";
    MembersDTO membersDTO;
    UserDetails userDetails;
    try {
      userDetails = loadUserByUsername(email);
    } catch (UsernameNotFoundException e) {
      log.error("User not found with email: " + email);
      return token;
    }
    log.info("serviceimpl result: " + userDetails);
    log.info("matches: " + passwordEncoder.matches(password, userDetails.getPassword()));
    if (passwordEncoder.matches(password, userDetails.getPassword())) {
      try {
        token = jwtUtil.generateToken(email);
        log.info("token:" + token);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return token;
  }

  @Override
  // MembersService.java

  public List<MembersDTO> getChatRoomMembers(Long chatRoomId) {
    return membersRepository.findByChatRoomId(chatRoomId).stream()
        .map(member -> new MembersDTO(member.getId(), member.getName()))
        .collect(Collectors.toList());

  }
}