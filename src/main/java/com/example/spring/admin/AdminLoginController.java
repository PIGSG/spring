package com.example.spring.admin;

import java.util.Collections;
import java.util.List;

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

    // 로그인 처리
    @PostMapping("/admin/login")
    public ModelAndView login(String userId, String password) {
        ModelAndView mav = new ModelAndView();
        
        System.out.println("🔹 Admin 로그인 시도 - userId: " + userId);
    
        if (adminDAO.authenticateAdmin(userId, password)) {
            System.out.println("✅ 로그인 성공 - Redirecting to /user");
    
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
    
            String redirectUrl = "redirect:/user";  // ✅ 명확한 리다이렉트 URL 설정
            System.out.println("🔹 Redirect URL 설정됨: " + redirectUrl);
    
            mav.setViewName(redirectUrl);
        } else {
            System.out.println("❌ 로그인 실패 - Redirecting to /admin/login?error=true");
            mav.setViewName("redirect:/admin/login?error=true");
        }
    
        return mav;
    }
    
}
