package com.demo.gram.config;



import com.demo.gram.security.filter.ApiCheckFilter;
import com.demo.gram.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 애플리케이션의 인증 및 권한 부여를 관리하고 보호
@Log4j2
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

  private static final String[] PERMIT_ALL_LIST = {
      "/auth/join", "/auth/login","/","/chat/**","/posts","/posts/**","/ws/**",
      "members/**",
      "/members/get/**", "/members/delete/**", "/members/update","/members/chatroom/**/members",
      "/members/chatroom/{chatRoomId}/members"
  };
  private static final String[] AUTHENTICATED_LIST = {
      "/follow/**", "/image/**", "/like/board"
  };

  // webSecurityCustomizer() 메서드는 요청에 대한 보안 설정을 커스터마이징합니다.
  // 여기서는 favicon과 error 페이지에 대한 요청을 무시하도록 설정합니다.
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> { // /error : spring에서 기본제공됨
      web.ignoring().requestMatchers("favicon.ico", "/error");
    };
  }

  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity, AuthenticationManager am) throws Exception {
    // 주소별 접근 권한 설정
    httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
      authorizationManagerRequestMatcherRegistry.requestMatchers(PERMIT_ALL_LIST).permitAll();
      authorizationManagerRequestMatcherRegistry.requestMatchers(AUTHENTICATED_LIST).authenticated();
      authorizationManagerRequestMatcherRegistry.anyRequest().denyAll();
    });

    // UsernamePasswordAuthenticationFilter 이전에 동작하도록 filter chain에 지정
    httpSecurity.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

    // csrf disabled
    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    //cache 없게 하기 :: 성능 저하가 발생할 수 있음
    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
    requestCache.setMatchingRequestParameterName(null);
    httpSecurity.requestCache(cache -> cache.requestCache(requestCache));

    // session stateless
    httpSecurity.sessionManagement(sessionManagementConfigurer ->
        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return httpSecurity.build();
  }

  // 사용자 비밀번호 암호화
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JWTUtil jwtUtil() {return new JWTUtil();}

  @Bean // 클라이언트로부터 요청된 주소에 권한이 있는지 없는지 확인
  public ApiCheckFilter apiCheckFilter() {
    String[] pattern = {"/auth/**", "/members/**", "/"}; // AntPathMatcher의 패턴 표기법
    return new ApiCheckFilter(pattern, jwtUtil());
  }

  @Bean
  // authentication bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration ac) throws Exception {
    return ac.getAuthenticationManager();
  }
}