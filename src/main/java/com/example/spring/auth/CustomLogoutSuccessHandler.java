package com.example.spring.auth;

import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.spring.users.UsersService;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UsersService usersService;

    public CustomLogoutSuccessHandler(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
        if (authentication != null && authentication.getName() != null) {
            String userId = authentication.getName();

            System.out.println("✅ [LogoutHandler] 로그아웃 실행됨 - userId: " + userId);

            // ✅ usersService.updateLastLoginTime()을 제거해도 됨 (DB가 자동 업데이트)
        }
        response.sendRedirect("/auth/login?logout");
    }
}
