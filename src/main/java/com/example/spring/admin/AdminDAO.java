package com.example.spring.admin;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDAO {

    @Autowired
    private SqlSession session;

    @Autowired
    private PasswordEncoder passwordEncoder; // BCryptPasswordEncoder

    private static final String namespace = "userMapper";

    // 관리자 인증
    public boolean authenticateAdmin(String userId, String rawPassword) {
        // DB에서 저장된 암호화된 비밀번호 가져오기
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        
        String encodedPassword = session.selectOne(namespace + ".getAdminPassword", params);

        if (encodedPassword == null) {
            return false; // 사용자가 존재하지 않음
        }

        // 입력된 비밀번호(rawPassword)와 DB의 암호화된 비밀번호 비교
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
