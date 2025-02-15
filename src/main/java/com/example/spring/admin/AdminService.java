package com.example.spring.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminDAO adminDAO;

    // 관리자 인증 메서드 호출
    public boolean authenticateAdmin(String userId, String password) {
        return adminDAO.authenticateAdmin(userId, password);
    }
}
