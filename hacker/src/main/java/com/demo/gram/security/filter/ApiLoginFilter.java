package com.demo.gram.security.filter;


import com.demo.gram.security.dto.AuthMemberDTO;
import com.demo.gram.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

// API 로그인 요청을 처리하고, 인증이 성공하면 JWT 토큰을 생성하여 클라이언트에게 반환
// 클라이언트의 API 로그인 요청을 처리할 수 있으며, 성공한 경우에는
// JWT 토큰을 생성하여 클라이언트에게 반환
// 이를 통해 클라이언트는 이후의 API 요청에서 해당 토큰을 사용하여 자신을 인증
@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

  private JWTUtil jwtUtil;

  public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
    super(defaultFilterProcessesUrl);
    this.jwtUtil = jwtUtil;
  }

  // 클라이언트의 로그인 요청을 처리하는 메서드
  // 클라이언트가 제공한 이메일과 비밀번호를 추출하여
  // UsernamePasswordAuthenticationToken 객체를 생성하고,
  // 해당 객체를 인증 관리자에게 전달하여 인증을 시도
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    log.info("ApiLoginFilter...attemptAuthentication");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
    log.info("authToken: " + authToken);
    return getAuthenticationManager().authenticate(authToken);
  }

  // 인증이 성공한 경우 호출되는 메서드
  // 인증 결과를 바탕으로 JWT 토큰을 생성하고, 이를 클라이언트에게 반환
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    log.info("ApiLoginFilter...successfulAuthentication...authResult:" + authResult);
    log.info("authResult.getPrincipal(): " + authResult.getPrincipal());
    String email = ((AuthMemberDTO)(authResult.getPrincipal())).getUsername();
    String token = null;
    try {
      token = jwtUtil.generateToken(email);
      response.setContentType("text/plain");
      response.getOutputStream().write(token.getBytes());
      log.info("token:" + token);
    } catch (Exception e) {e.printStackTrace();}
  }
}
