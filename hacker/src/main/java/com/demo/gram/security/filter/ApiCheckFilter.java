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
import org.json.JSONObject;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.io.PrintWriter;

// API 엔드포인트에 대한 요청을 검사하여 JWT 토큰이 있는지 확인하고, 토큰이 유효한지 검증하는 것
@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
  private String[] pattern;
  private JWTUtil jwtUtil;

  public ApiCheckFilter(String[] pattern, JWTUtil jwtUtil) {
    this.pattern = pattern; this.jwtUtil = jwtUtil;
  }

  //  클라이언트의 요청이 들어오면 해당 메서드가 호출되며,
  //  요청에 대한 필터링이 수행@Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                FilterChain filterChain) throws ServletException, IOException {
    log.info("REQUEST URI: " + request.getRequestURI());
    String requestPath = request.getRequestURI();
    
    // 예외 경로 리스트
    String[] permitAllPaths = {"/auth/join", "/auth/login"};
    
    // 요청 경로가 예외 경로 중 하나인지 검사
    for (String path : permitAllPaths) {
        if (requestPath.startsWith(request.getContextPath() + path)) {
            filterChain.doFilter(request, response);
            return;
        }
    }

    // 이전과 동일한 패턴 검사 및 JWT 검증 로직
    boolean check = false;
    for (String patternStr : this.pattern) {
        PathPattern pattern = new PathPatternParser().parse(antpathToPathpattern2(patternStr));
        if (pattern.matches(PathContainer.parsePath(requestPath))) {
            check = true;
            break;
        }
    }

    if (check) {
        if (checkAuthHeader(request)) { // JWT 있고 유효할 때
            filterChain.doFilter(request, response);
        } else { // JWT 없거나 유효하지 않을 때
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "403");
            jsonObject.put("message", "FAIL CHECK API TOKEN");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(jsonObject);
        }
        return;
    }

    // 다른 경우에는 필터 체인 계속
    filterChain.doFilter(request, response);
}


  // checkAuthHeader: 클라이언트 요청의 헤더에서 JWT 토큰을 추출하고,
  // 해당 토큰이 올바른지 검증하는 메서드
  private boolean checkAuthHeader(HttpServletRequest request) {
    boolean checkResult = false;
    String authHeader = request.getHeader("Authorization");
    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      log.info("Authorization exist: " + authHeader);
      try {
        String email = jwtUtil.validateAndExtract(authHeader.substring(7));
        log.info("validate result: " + email);
        checkResult = email.length() > 0;
      } catch (Exception e){e.printStackTrace();}
    }
    return checkResult;
  }

  private String antpathToPathpattern2(String str) {
//    str = str.substring(0, str.indexOf("**")) + "{*path}" + str.substring(str.indexOf("**")+2);
    str = str.replace("**", "{*path}");
    log.info("antpathToPathpattern2 : " + str);
    return str;
  }
}
