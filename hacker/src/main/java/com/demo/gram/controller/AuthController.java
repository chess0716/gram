package com.demo.gram.controller;


import com.demo.gram.dto.MembersDTO;
import com.demo.gram.dto.ResponseDTO;
import com.demo.gram.security.util.JWTUtil;
import com.demo.gram.service.MembersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@Log4j2
@RequestMapping("auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

  private final MembersService membersService;
  private final JWTUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;


  // 회원가입
  @PostMapping(value = "/join")
  public ResponseEntity<Long> register(@RequestBody MembersDTO membersDTO) {
    log.info("register..."+membersDTO);

    long num = membersService.registMembersDTO(membersDTO);
    return new ResponseEntity<>(num, HttpStatus.OK);
  }

  // 로그인
  // (클라이언트가 보낸 이메일과 비밀번호를 사용하여 로그인을 시도, 성공하면 JWT 토큰을 생성하여 Map에 담아 반환)
  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> getToken(@RequestBody Map<String, String> loginRequest) {
    String email = loginRequest.get("email");
    String password = loginRequest.get("password");

    // 이메일 또는 비밀번호가 없는 경우
    if (email == null || password == null) {
      String errorMessage = "Email or password is missing in the login request";
      log.error(errorMessage);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", errorMessage));
    }

    log.info("Login attempt with email: " + email);

    try {
      // 사용자 인증 및 토큰 생성
      log.info("Authenticating user...");
      String token = membersService.login(email, password, jwtUtil);

      // 토큰이 유효하면 반환
      if (token != null && !token.isEmpty()) {
        log.info("Login successful. Generating token...");
        Map<String, String> response = Collections.singletonMap("token", token);
        return ResponseEntity.ok(response);
      } else {
        String errorMessage = "Login failed. Invalid email or password.";
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", errorMessage));
      }
    } catch (Exception e) {
      String errorMessage = "An error occurred during login: " + e.getMessage();
      log.error(errorMessage, e); // 예외 객체와 함께 로깅
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", errorMessage));
    }
  }


  // 로그아웃
  // 로그아웃 시 토큰 기록이 사라지게 하고 싶은 경우 서버보다 클라이언트측에서 코드 작성하는 것이 더 좋음
  @PostMapping("/logout")
  public ResponseEntity<ResponseDTO> logout(HttpServletRequest request, HttpServletResponse response) {
    try {
      log.info(request);
      log.info(response);
      log.info(SecurityContextHolder.getContext().getAuthentication());
      new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
      return new ResponseEntity<>(new ResponseDTO("success", true), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseDTO(e.getMessage(), false), HttpStatus.NOT_FOUND);
    }
  }
}
