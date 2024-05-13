package com.demo.gram.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

  private String secretKey = "1234567890abcdefghijklmnopqrstuvwxyz";
  private long expire = 60 * 24 * 30;

  // JWT를 생성
  public String generateToken(String content) throws Exception {
    return Jwts.builder()
        .issuedAt(new Date()) // 발급 시간
        .expiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant())) //  만료 시간
        .claim("sub", content)
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
  }

  // 주어진 JWT의 유효성을 검증하고, 유효한 경우에는 내부에 포함된 클레임을 추출하여 반환
  public String validateAndExtract(String tokenStr) throws Exception {
    log.info("Jwts getClass: " +
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build().parse(tokenStr));
    Claims claims = (Claims) Jwts.parser().verifyWith(
        Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).build().parse(tokenStr).getPayload();
    return (String) claims.get("sub");
  }
}
