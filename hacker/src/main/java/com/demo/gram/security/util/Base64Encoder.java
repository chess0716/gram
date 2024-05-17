package com.demo.gram.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class Base64Encoder {
  public static void main(String[] args) {
    byte[] key = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
    String encodedKey = Base64.getEncoder().encodeToString(key);
    System.out.println(encodedKey);
  }
}
