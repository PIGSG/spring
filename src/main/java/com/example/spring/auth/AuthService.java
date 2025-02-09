package com.example.spring.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring.users.UserDao;  // UsersDao → UserDao로 변경
import com.example.spring.users.UsersVo;

@Service
public class AuthService {

    @Autowired
    UserDao userDao;  // UsersDao → UserDao로 변경

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 로그인
    public UsersVo login(UsersVo usersVo) {
        UsersVo existUsersVo = userDao.read(usersVo);  // UsersDao → UserDao로 변경

        if (existUsersVo != null && passwordEncoder.matches(usersVo.getPassword(), existUsersVo.getPassword())) {
            return existUsersVo;
        }

        return null;
    }

    // 비밀번호 초기화
    public String resetPassword(UsersVo usersVo) {
        // 랜덤 비밀번호 생성
        String rndPassword = UUID.randomUUID().toString().substring(0, 8);
        String encodedPassword = passwordEncoder.encode(rndPassword);
        usersVo.setPassword(encodedPassword);

        int updated = userDao.update(usersVo);  // UsersDao → UserDao로 변경

        if (updated > 0) {
            return rndPassword;
        } else {
            return null;
        }
    }
}
