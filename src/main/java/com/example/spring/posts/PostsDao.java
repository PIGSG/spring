package com.example.spring.posts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostsDao {

    @Autowired
    private SqlSession session;

    private static final String NAMESPACE = "postsMapper"; // 상수명 변경

    // 게시글 등록
    public int create(PostsVo postsVo) {
        return session.insert(NAMESPACE + ".create", postsVo); // 변경된 상수 사용
    }

    // 게시글 목록
    public List<PostsVo> list(int offset, int pageSize, String searchType, String searchKeyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        return session.selectList(NAMESPACE + ".list", params); // 변경된 상수 사용
    }

    // 게시글 보기
    public PostsVo read(int id) {
        return session.selectOne(NAMESPACE + ".read", id); // 변경된 상수 사용
    }

    // 게시글 수정
    public int update(PostsVo postsVo) {
        return session.update(NAMESPACE + ".update", postsVo); // 변경된 상수 사용
    }

    // 게시글 삭제
    public int delete(int id) {
        return session.delete(NAMESPACE + ".delete", id); // 변경된 상수 사용
    }

    // 전체 게시글 수
    public int getTotalCount(String searchType, String searchKeyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        return session.selectOne(NAMESPACE + ".getTotalCount", params); // 변경된 상수 사용
    }
}
