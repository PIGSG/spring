package com.example.spring.auth;

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

    // ✅ 비밀번호 초기화 (임시 비밀번호 발급)
    public String resetPassword(UsersVo usersVo) {
        String temporaryPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(temporaryPassword);
        usersVo.setPassword(encodedPassword);

        usersDao.update(usersVo);
        return temporaryPassword;
    }

    // ✅ 임시 비밀번호 생성 (8자리 랜덤 문자열)
    private String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
