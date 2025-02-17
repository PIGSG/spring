package com.example.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.spring.auth.CustomLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final HandlerMappingIntrospector handlerMappingIntrospector;

    public SecurityConfig(CustomLogoutSuccessHandler logoutSuccessHandler, HandlerMappingIntrospector handlerMappingIntrospector) {
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.handlerMappingIntrospector = handlerMappingIntrospector;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 홈, auth 관련, 그리고 admin 로그인 페이지는 모두 접근 가능하도록 설정
                .requestMatchers("/", "/auth/login", "/auth/logout","/admin/login").permitAll()
                // /user/** 는 ROLE_ADMIN 권한이 있어야 접근 가능
                .requestMatchers("/user/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")              // 로그아웃 처리 URL
                .logoutSuccessHandler(logoutSuccessHandler)  // 커스텀 로그아웃 성공 핸들러
                .logoutSuccessUrl("/auth/login?logout") // 로그아웃 후 이동할 URL
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}
