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

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
  private String[] pattern;
  private JWTUtil jwtUtil;

  public ApiCheckFilter(String[] pattern, JWTUtil jwtUtil) {
    this.pattern = pattern;
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("REQUEST URI: " + request.getRequestURI());
    String requestPath = request.getRequestURI();

    String[] permitAllPaths = {"/auth/join", "/auth/login", "/members/me", "/members/update", "/auth/logout", "/members/delete/**"};

    for (String path : permitAllPaths) {
      if (requestPath.startsWith(request.getContextPath() + path)) {
        filterChain.doFilter(request, response);
        return;
      }
    }

    boolean check = false;
    for (String patternStr : this.pattern) {
      PathPattern pattern = new PathPatternParser().parse(antpathToPathpattern2(patternStr));
      if (pattern.matches(PathContainer.parsePath(requestPath))) {
        check = true;
        break;
      }
    }

    if (check) {
      if (checkAuthHeader(request)) {
        filterChain.doFilter(request, response);
        log.info("토큰 있음");
      } else {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.print("{\"code\":\"403\",\"message\":\"FAIL CHECK API TOKEN\"}");
      }
      return;
    }

    filterChain.doFilter(request, response);
  }

  private boolean checkAuthHeader(HttpServletRequest request) {
    boolean checkResult = false;
    log.info("검증되고 있음");
    String authHeader = request.getHeader("Authorization");
    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      log.info("Authorization exist: " + authHeader);
      try {
        String email = jwtUtil.validateAndExtract(authHeader.substring(7));
        log.info("validate result: " + email);
        checkResult = email.length() > 0;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return checkResult;
  }

  private String antpathToPathpattern2(String str) {
    str = str.replace("**", "{*path}");
    log.info("antpathToPathpattern2 : " + str);
    return str;
  }
}
