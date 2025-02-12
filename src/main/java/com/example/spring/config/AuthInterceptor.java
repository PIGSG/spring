package com.example.spring.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull Object handler
    ) throws Exception {
        HttpSession session = request.getSession(false);
        
<<<<<<< HEAD
        if (session == null || session.getAttribute("isLoggedIn") == null) {
=======
        if (session == null || session.getAttribute("userId") == null) {
>>>>>>> e6083e0 (Initial commit)
            response.sendRedirect("/auth/login?error=auth");
            return false;
        }
        
        return true;
    }
}