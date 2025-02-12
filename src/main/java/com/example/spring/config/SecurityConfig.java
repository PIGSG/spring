package com.example.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;  // SecurityFilterChain import
import org.springframework.web.servlet.config.annotation.EnableWebMvc;  // HandlerMappingIntrospector import
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;  // EnableWebMvc import

@Configuration
@EnableWebSecurity
@EnableWebMvc  // Spring MVC 설정 활성화
public class SecurityConfig {
    
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorizeRequests -> 
            authorizeRequests
                .requestMatchers("/login", "/register", "/").permitAll()
                .requestMatchers("/posts/**").authenticated()  // 게시글 관련 페이지는 로그인 필요
                .anyRequest().authenticated()
        )
        .formLogin(form -> 
            form.loginPage("/auth/login").permitAll()
        )
        .logout(logout -> 
            logout.logoutUrl("/auth/logout").logoutSuccessUrl("/auth/login").invalidateHttpSession(true).deleteCookies("JSESSIONID")
        )
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 유지 설정
        );
    return http.build();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 암호화 설정
    }

    // mvcHandlerMappingIntrospector 빈 추가
    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}
