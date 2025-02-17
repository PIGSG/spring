package com.example.spring.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import com.example.spring.users.UsersVo;

public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull Object handler
    ) throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI(); // 현재 요청된 URL

        // ✅ 로그인 체크를 제외할 URL 목록
        if (requestURI.startsWith("/auth/login") || requestURI.startsWith("/admin/login") || requestURI.startsWith("/resources/")) {
            return true;  // 로그인 없이 접근 가능
        }

        // ✅ 세션이 없는 경우 로그인 페이지로 리디렉션
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/auth/login?error=auth");
            return false;
        }

        // ✅ 세션에서 사용자 정보 가져오기
        UsersVo user = (UsersVo) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/auth/login?error=auth");
            return false;
        }

        String role = user.getRole(); // 사용자 역할 가져오기

        // ✅ 관리자 페이지 접근 제한 (ROLE_ADMIN만 가능)
        if (requestURI.startsWith("/user") && !"ROLE_ADMIN".equals(role)) {
            response.sendRedirect("/auth/login?error=auth");
            return false;
        }

        return true;
    }
}
