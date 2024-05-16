package com.demo.gram.service;

import com.demo.gram.dto.MembersDTO;
import com.demo.gram.entity.ChatRoom;
import com.demo.gram.entity.Members;
import com.demo.gram.repository.ChatRoomRepository;
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
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService, UserDetailsService {

  private final MembersRepository membersRepository;
  private final PasswordEncoder passwordEncoder;
  private final ChatRoomRepository chatRoomRepository;
  private final JWTUtil jwtUtil;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Members user = membersRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .roles("USER")
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
    return result.map(this::entityToDTO).orElse(null);
  }

  @Override
  public List<MembersDTO> getAll() {
    List<Members> membersList = membersRepository.getAll();
    return membersList.stream().map(this::entityToDTO).collect(Collectors.toList());
  }

  @Override
  public String login(String email, String password, JWTUtil jwtUtil) {
    log.info("login............");
    String token = "";
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
  public List<MembersDTO> getChatRoomMembers(Long chatRoomId) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new RuntimeException("Chat room not found"));
    return chatRoom.getMembers().stream().map(this::entityToDTO).collect(Collectors.toList());
  }

  @Override
  public Members findByEmail(String email) {
    return membersRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
  }

  public void joinChatRoom(String email, Long chatRoomId) {
    Members member = membersRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new RuntimeException("Chat room not found"));

    member.joinChatRoom(chatRoom);
    membersRepository.save(member);
  }

  public Members findByToken(String token) {
    try {
      String email = jwtUtil.validateAndExtract(token);
      return findByEmail(email);
    } catch (Exception e) {
      throw new RuntimeException("Invalid token", e);
    }
  }
}
