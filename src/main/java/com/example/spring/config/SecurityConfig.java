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
                .requestMatchers("/", "/auth/login", "/auth/logout").permitAll()  // ✅ 인증 없이 접근 가능하도록 수정
                .requestMatchers("/user/**").hasRole("ADMIN") // ✅ 관리자만 접근 가능
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandler)  // ✅ 커스텀 로그아웃 핸들러 적용
                .logoutSuccessUrl("/auth/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ MvcRequestMatcher가 동작할 수 있도록 Bean 등록
    @Bean
    public static HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}
