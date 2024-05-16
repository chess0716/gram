package com.demo.gram.service;


import com.demo.gram.dto.MembersDTO;
import com.demo.gram.entity.Members;
import com.demo.gram.entity.MembersRole;
import com.demo.gram.security.util.JWTUtil;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface MembersService {

  default Members dtoToEntity(MembersDTO membersDTO) {
    Members members = Members.builder()
        .mno(membersDTO.getMno())
        .id(membersDTO.getId())
        .password(membersDTO.getPassword())
        .email(membersDTO.getEmail())
        .name(membersDTO.getName())
        .mobile(membersDTO.getMobile())
        .fromSocial(membersDTO.isFromSocial())
        .regDate(membersDTO.getRegDate())
        .modDate(membersDTO.getModDate())
        .roleSet(membersDTO.getRoleSet().stream().map(str -> {
          if(str.equals("ROLE_USER")) return MembersRole.USER;
          else if (str.equals("ROLE_MANAGER")) return MembersRole.MANAGER;
          else if (str.equals("ROLE_ADMIN")) return MembersRole.ADMIN;
          else return MembersRole.USER;
        }).collect(Collectors.toSet()))
        .build();
    return members;
  }

  default MembersDTO entityToDTO(Members members) {
    MembersDTO membersDTO = MembersDTO.builder()
        .mno(members.getMno())
        .id(members.getId())
        .password(members.getPassword())
        .email(members.getEmail())
        .name(members.getName())
        .mobile(members.getMobile())
        .fromSocial(members.isFromSocial())
        .regDate(members.getRegDate())
        .modDate(members.getModDate())
        .roleSet(members.getRoleSet().stream().map(
            new Function<MembersRole, String>() {
              @Override
              public String apply(MembersRole role) {
                return new String("ROLE_" + role.name());
              }
            }).collect(Collectors.toSet()))
        .build();
    return membersDTO;
  }

  Long registMembersDTO(MembersDTO membersDTO);

  void updateMembersDTO(MembersDTO membersDTO);

  void removeMembers(Long num);

  MembersDTO get(Long num);

  List<MembersDTO> getAll();

  String login(String email, String password, JWTUtil jwtUtil);
  List<MembersDTO> getChatRoomMembers(Long chatRoomId);
  Members findByEmail(String email);
  void joinChatRoom(String email, Long chatRoomId);
}
