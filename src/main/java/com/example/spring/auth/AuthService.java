package com.example.spring.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring.users.UserDao;
import com.example.spring.users.UsersVo;

@Service
public class AuthService {

    private final UserDao usersDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserDao usersDao, PasswordEncoder passwordEncoder) {
        this.usersDao = usersDao;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ 로그인 처리 메서드
    public UsersVo login(UsersVo usersVo) {
        UsersVo storedUser = usersDao.findByUserId(usersVo.getUserId());

        if (storedUser != null && passwordEncoder.matches(usersVo.getPassword(), storedUser.getPassword())) {
            return storedUser; // 로그인 성공
        }
        return null; // 로그인 실패
    }

    // ✅ 비밀번호 검증 메서드 (checkPassword) 추가
    public boolean checkPassword(String userId, String rawPassword) {
        UsersVo storedUser = usersDao.findByUserId(userId);
        if (storedUser == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, storedUser.getPassword());
    }

    // 비밀번호 초기화
    public String resetPassword(UsersVo usersVo) {
        // 랜덤 비밀번호 생성
        String rndPassword = UUID.randomUUID().toString().substring(0, 8);
        String encodedPassword = passwordEncoder.encode(rndPassword);


            // 기존 유저 정보 가져오기 (userId 포함)
        UsersVo existingUser = usersDao.read(usersVo);
        if (existingUser == null) {
            return null; // 존재하지 않는 사용자
        }

        // 비밀번호 업데이트
        existingUser.setPassword(encodedPassword);
        int updated = usersDao.update(existingUser);

        if (updated > 0) {
            return rndPassword;
        } else {
            return null;
        }

    }
}