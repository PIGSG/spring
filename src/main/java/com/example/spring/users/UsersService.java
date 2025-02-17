package com.example.spring.users;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring.libs.Pagination;

@Service
public class UsersService implements UserDetailsService {  // ✅ UserDetailsService 구현

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final SqlSession sqlSession;  // ✅ SqlSession 추가

    @Autowired
    public UsersService(UserDao userDao, PasswordEncoder passwordEncoder, SqlSession sqlSession) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.sqlSession = sqlSession;
    }

    // ✅ UserDetailsService 구현 (Spring Security용)
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UsersVo user = userDao.findByUserId(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.withUsername(user.getUserId())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
                .build();
    }

public void updateLastLogin(String userId) {
    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);
    params.put("lastLoginAt", LocalDateTime.now());  // 현재 시간 추가

    sqlSession.update("userMapper.updateLastLogin", params);
    System.out.println("✅ LAST_LOGIN_AT 업데이트 완료 - userId: " + userId);
}

    // ✅ 사용자 생성 (비밀번호 암호화 적용)
    public boolean create(UsersVo usersVo) {
        String encodedPassword = passwordEncoder.encode(usersVo.getPassword());
        usersVo.setPassword(encodedPassword);
        return userDao.create(usersVo) > 0;
    }

    // ✅ 사용자 정보 조회
    public UsersVo read(UsersVo usersVo) {
        return userDao.read(usersVo);
    }

    // ✅ 사용자 정보 업데이트
    public boolean update(UsersVo usersVo) {
        return userDao.update(usersVo) > 0;
    }

    // 비밀번호 수정
    public boolean updatePassword(UsersVo usersVo) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(usersVo.getPassword());
        usersVo.setPassword(encodedPassword);

        int result = userDao.update(usersVo);
        return result > 0;
    }

    // ✅ 사용자 목록 조회 (페이징 처리 포함)
    public Map<String, Object> list(int page, String searchType, String searchKeyword) {
        int pageSize = 10;
        int totalCount = userDao.getTotalCount(searchType, searchKeyword);
        Pagination pagination = new Pagination(page, pageSize, totalCount);
        List<UsersVo> userVoList = userDao.list(pagination.getOffset(), pageSize, searchType, searchKeyword);

        Map<String, Object> result = new HashMap<>();
        result.put("userVoList", userVoList);
        result.put("pagination", pagination);
        result.put("searchType", searchType);
        result.put("searchKeyword", searchKeyword);
        return result;
    }



        // 사용자 삭제
        public boolean delete(UsersVo usersVo, String password) {
            // DB에서 사용자 정보 조회 (usersVo에 userId, username, email 등이 설정되어 있음)
            UsersVo dbUser = userDao.read(usersVo);
            if (dbUser == null) {
                return false;  // 해당 사용자가 존재하지 않음
            }
            
            // 입력받은 이름과 이메일이 DB 정보와 일치하는지 확인
            if (!dbUser.getUsername().equals(usersVo.getUsername()) ||
                !dbUser.getEmail().equals(usersVo.getEmail())) {
                return false;
            }
            
            // 입력받은 비밀번호가 DB에 저장된 암호화된 비밀번호와 일치하는지 확인
            if (!passwordEncoder.matches(password, dbUser.getPassword())) {
                return false;
            }
            
            // 모든 조건이 만족되면 삭제 실행
            int result = userDao.delete(dbUser.getUserId());
            return result > 0;
        }
}
