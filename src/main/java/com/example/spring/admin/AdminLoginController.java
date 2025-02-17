package com.example.spring.admin;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminLoginController {

    @Autowired
    private AdminDAO adminDAO; // AdminDAO 의존성 주입


    @PostMapping("/admin/login")
    public ModelAndView login(String userId, String password, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        
        System.out.println("🔹 Admin 로그인 시도 - userId: " + userId);
    
        if (adminDAO.authenticateAdmin(userId, password)) {
            System.out.println("✅ 로그인 성공 - Redirecting to /user");
    
            // ✅ Spring Security 인증 객체 설정
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
    
            // ✅ 관리자 정보 세션에 저장
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", userId);
            session.setAttribute("role", "ROLE_ADMIN");  // ✅ 관리자 역할 저장
    
            // ✅ 로그 출력 추가 (세션 값 확인)
            System.out.println("🔹 세션 저장 완료: userId=" + session.getAttribute("userId") + ", role=" + session.getAttribute("role"));
    
            mav.setViewName("redirect:/user");
        } else {
            System.out.println("❌ 로그인 실패 - Redirecting to /admin/login?error=invalid");
            mav.setViewName("redirect:/admin/login?error=invalid");
        }
    
        return mav;
    }
    
    

    
}
