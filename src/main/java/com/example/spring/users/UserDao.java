package com.example.spring.users;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private SqlSession sqlSession;

    // 사용자 정보 조회 (userId 기준)
public UsersVo findByUserId(String userId) {
    return sqlSession.selectOne("userMapper.findByUserId", userId);
}

    
public int updateLastLogin(String userId, LocalDateTime logoutTime) {
    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);
    params.put("lastLoginAt", logoutTime);

    System.out.println("🔹 [UserDao] updateLastLogin() 실행 - userId: " + userId + ", lastLoginAt: " + logoutTime);
    
    int result = sqlSession.update("userMapper.updateLastLogin", params);
    
    if (result > 0) {
        System.out.println("✅ [UserDao] LAST_LOGIN_AT 업데이트 성공!");
    } else {
        System.out.println("⚠️ [UserDao] LAST_LOGIN_AT 업데이트 실패! (0행 업데이트)");
    }

    return result;
}





    // 사용자 등록
    public int create(UsersVo usersVo) {
        return sqlSession.insert("userMapper.create", usersVo);
    }

    // 사용자 정보
    public UsersVo read(UsersVo usersVo) {
        return sqlSession.selectOne("userMapper.read", usersVo);
    }

    // 사용자 수정
    public int update(UsersVo usersVo) {
        return sqlSession.update("userMapper.update", usersVo);
    }

    // 사용자 목록
    public List<UsersVo> list(int offset, int pageSize, String searchType, String searchKeyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        return sqlSession.selectList("userMapper.list", params);
    }

    // 사용자 전체 수
    public int getTotalCount(String searchType, String searchKeyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        return sqlSession.selectOne("userMapper.getTotalCount", params);
    }

    // ✅ 중복된 delete() 메서드 제거 후 수정
    public int delete(String userId) {
        return sqlSession.delete("userMapper.delete", userId); // 🔹 userMapper.delete 호출
    }



    
}

