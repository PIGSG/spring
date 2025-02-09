package com.example.spring.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring.libs.Pagination;


@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 사용자 등록
    public boolean create(UsersVo usersVo) {  // UserVo → UsersVo로 변경
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(usersVo.getPassword());
        usersVo.setPassword(encodedPassword);

        int result = userDao.create(usersVo);
        return result > 0;
    }

    // 사용자 정보 조회
    public UsersVo read(UsersVo usersVo) {  // UserVo → UsersVo로 변경
        return userDao.read(usersVo);
    }

    // 사용자 수정
    public boolean update(UsersVo usersVo) {  // UserVo → UsersVo로 변경
        int result = userDao.update(usersVo);
        return result > 0;
    }

    // 비밀번호 수정
    public boolean updatePassword(UsersVo usersVo) {  // UserVo → UsersVo로 변경
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(usersVo.getPassword());
        usersVo.setPassword(encodedPassword);

        int result = userDao.update(usersVo);
        return result > 0;
    }

    // 사용자 목록 조회
    public Map<String, Object> list(int page, String searchType, String searchKeyword) {
        int pageSize = 10;  // 페이지당 사용자 수

        // 전체 사용자 수 조회
        int totalCount = userDao.getTotalCount(searchType, searchKeyword);

        // 페이지네이션 정보 생성
        Pagination pagination = new Pagination(page, pageSize, totalCount);

        // 페이징된 사용자 목록 조회
        List<UsersVo> usersVoList = userDao.list(pagination.getOffset(), pageSize, searchType, searchKeyword);

        // 결과 맵 생성
        Map<String, Object> result = new HashMap<>();
        result.put("usersVoList", usersVoList);  // 변수명 UsersVo로 수정
        result.put("pagination", pagination);
        result.put("searchType", searchType);
        result.put("searchKeyword", searchKeyword);

        return result;
    }

    // 사용자 삭제
    public boolean delete(UsersVo usersVo, String password) {  // UserVo → UsersVo로 변경
        // 비밀번호 검증
        UsersVo existUser = read(usersVo);
        if (!passwordEncoder.matches(password, existUser.getPassword())) {
            return false;
        }

        int result = userDao.delete(usersVo.getUserId());
        return result > 0;
    }
}