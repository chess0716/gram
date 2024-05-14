package com.demo.gram.security.filter;

import com.demo.gram.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.server.PathContainer;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
  private final List<PathPattern> patterns;
  private final JWTUtil jwtUtil;

  public ApiCheckFilter(List<String> patternStrings, JWTUtil jwtUtil) {
    PathPatternParser parser = new PathPatternParser();
    this.patterns = patternStrings.stream().map(parser::parse).toList();
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    // 모든 경로 허용으로 인해 인증 검사 생략
    filterChain.doFilter(request, response);
  }
}
