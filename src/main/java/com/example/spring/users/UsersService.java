package com.example.spring.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    UsersDao usersDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 사용자 등록
    public boolean create(UsersVo usersVo) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(usersVo.getPassword());
        usersVo.setPassword(encodedPassword);

        int result = usersDao.create(usersVo);
        return result > 0;
    }

    // 사용자 보기
    public UsersVo read(UsersVo usersVo) {
        return usersDao.read(usersVo);
    }

    // 사용자 수정
    public boolean update(UsersVo usersVo) {
        int result = usersDao.update(usersVo);
        return result > 0;
    }

    // 비밀번호 수정
    public boolean updatePassword(UsersVo usersVo) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(usersVo.getPassword());
        usersVo.setPassword(encodedPassword);

        int result = usersDao.update(usersVo);
        return result > 0;
    }

    // 사용자 삭제
    public boolean delete(UsersVo usersVo) {
        int result = usersDao.delete(usersVo);
        return result > 0;
    }
}
