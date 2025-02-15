package com.example.spring.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    // 관리자 로그인 페이지
    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin/login";  // login.jsp 페이지로 이동
    }

}
