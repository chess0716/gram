package com.demo.gram.service;

import com.demo.gram.dto.ChatRoomDTO;
import com.demo.gram.dto.MembersDTO;
import com.demo.gram.dto.MyPostDTO;
import com.demo.gram.entity.ChatRoom;
import com.demo.gram.entity.Members;
import com.demo.gram.entity.MyPostEntity;
import com.demo.gram.repository.ChatRoomRepository;
import com.demo.gram.repository.MembersRepository;
import com.demo.gram.repository.MyPostRepository;
import com.demo.gram.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService {

  private final MembersRepository membersRepository;
  private final MyPostRepository myPostRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWTUtil jwtUtil;
  private final ModelMapper modelMapper;

  @Override
  public Long registMembersDTO(MembersDTO membersDTO) {
    membersDTO.setPassword(passwordEncoder.encode(membersDTO.getPassword()));
    return membersRepository.save(dtoToEntity(membersDTO)).getMno();
  }

  @Override
  public void updateMembersDTO(MembersDTO membersDTO) {
    if (membersDTO.getMno() == null) {
      throw new IllegalArgumentException("Member ID (mno) must not be null");
    }
    Members existingMember = membersRepository.findById(membersDTO.getMno())
        .orElseThrow(() -> new IllegalArgumentException("Member not found"));

    existingMember.setName(membersDTO.getName());
    if (membersDTO.getPassword() != null && !membersDTO.getPassword().isEmpty()) {
      existingMember.setPassword(passwordEncoder.encode(membersDTO.getPassword()));
    }
    existingMember.setMobile(membersDTO.getMobile());
    membersRepository.save(existingMember);
  }

  @Override
  public void removeMembers(Long mno) {
    membersRepository.deleteById(mno);
  }

  @Override
  public MembersDTO get(Long mno) {
    Optional<Members> result = membersRepository.findById(mno);
    return result.map(this::entityToDTO).orElse(null);
  }

  @Override
  public List<MembersDTO> getAll() {
    List<Members> membersList = membersRepository.findAll();
    return membersList.stream().map(this::entityToDTO).collect(Collectors.toList());
  }

  @Override
  public String login(String email, String password, JWTUtil jwtUtil) {
    Members user = membersRepository.findByEmail(email, false)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    if (passwordEncoder.matches(password, user.getPassword())) {
      try {
        return jwtUtil.generateToken(email);
      } catch (Exception e) {
        log.error("Error generating token: ", e);
        return "";
      }
    } else {
      return "";
    }
  }

  @Override
  public Members getCurrentLoggedInUser(String token) throws Exception {
    String email = jwtUtil.validateAndExtract(token);
    return membersRepository.findByEmail(email, false)
        .orElseThrow(() -> new Exception("Logged in user not found"));
  }

  @Override
  public List<MyPostDTO> getUserPostsFromToken(String token) throws Exception {
    Long mno = getMnoFromToken(token);
    return getUserPosts(mno);
  }

  @Override
  public List<ChatRoomDTO> getUserChatRoomsFromToken(String token) throws Exception {
    Long mno = getMnoFromToken(token);
    return getUserChatRooms(mno);
  }

  @Override
  public Long getUserIdFromToken(String token) throws Exception {
    String email = jwtUtil.validateAndExtract(token);
    Members user = membersRepository.findByEmail(email, false)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return user.getMno();
  }

  @Override
  public List<MembersDTO> getChatRoomMembers(Long chatRoomId) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));
    return chatRoom.getMembers().stream()
        .map(this::entityToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public Members findByEmail(String email) {
    return membersRepository.findByEmail(email, false)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }

  @Override
  public void joinChatRoom(String email, Long chatRoomId) {
    Members member = findByEmail(email);
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));
    member.joinChatRoom(chatRoom);
    membersRepository.save(member);
  }

  private List<MyPostDTO> getUserPosts(Long mno) {
    List<MyPostEntity> userPosts = myPostRepository.findByMember_Mno(mno);
    return userPosts.stream()
        .map(post -> modelMapper.map(post, MyPostDTO.class))
        .collect(Collectors.toList());
  }

  private List<ChatRoomDTO> getUserChatRooms(Long mno) {
    List<ChatRoom> userChatRooms = chatRoomRepository.findByMembers_Mno(mno);
    return userChatRooms.stream()
        .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomDTO.class))
        .collect(Collectors.toList());
  }

  private Long getMnoFromToken(String token) throws Exception {
    String email = jwtUtil.validateAndExtract(token);
    Members user = membersRepository.findByEmail(email, false)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return user.getMno();
  }
}
