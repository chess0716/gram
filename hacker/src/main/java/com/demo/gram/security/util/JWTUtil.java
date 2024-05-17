package com.demo.gram.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.Jwts.*;

@Log4j2
@Component
public class JWTUtil {

  @Value("${jwt.secret}")
  private String secret;

  private Key secretKey;

  private final long expire = 60 * 24 * 30 * 1000; // 30 days in milliseconds

  @PostConstruct
  public void init() {
    byte[] decodedKey = Base64.getDecoder().decode(secret);
    this.secretKey = Keys.hmacShaKeyFor(decodedKey); // Use the provided key
  }

  public String generateToken(String content) {
    return builder()
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expire))
        .claim("sub", content)
        .signWith(secretKey, SignatureAlgorithm.HS512)
        .compact();
  }

  public String validateAndExtract(String tokenStr) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(tokenStr)
        .getBody();
    return claims.get("sub", String.class);
  }
}
