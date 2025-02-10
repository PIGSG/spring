package com.example.spring.users;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersDao {

    @Autowired
    private SqlSession session;

    private static final String namespace = "usersMapper";

    // 사용자 등록
    public int create(UsersVo usersVo) {
        return session.insert(namespace + ".create", usersVo);
    }

    // 사용자 보기
    public UsersVo read(UsersVo usersVo) {
        return session.selectOne(namespace + ".read", usersVo);
    }

    // 사용자 수정
    public int update(UsersVo usersVo) {
        return session.update(namespace + ".update", usersVo);
    }

    // 사용자 삭제
    public int delete(UsersVo usersVo) {
        return session.delete(namespace + ".delete", usersVo);
    }
}
