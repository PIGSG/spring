package com.example.spring.posts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring.libs.Pagination;

@Service
public class PostsService {

    @Autowired
    PostsDao postsDao;

    // 게시글 등록
    public boolean create(PostsVo postsVo) {
        int result = postsDao.create(postsVo);
        return result > 0;
    }

    // 게시글 목록
    public Map<String, Object> list(int page, int pageSize, String searchType, String searchKeyword) {
        // 전체 게시글 수 조회
        int totalCount = postsDao.getTotalCount(searchType, searchKeyword);

        // 페이지네이션 정보 생성
        Pagination pagination = new Pagination(page, pageSize, totalCount);

        // 페이징된 게시글 목록 조회
        List<PostsVo> postsVoList = postsDao.list(pagination.getOffset(), pageSize, searchType, searchKeyword);

        // 결과 맵 생성
        Map<String, Object> result = new HashMap<>();
        result.put("postsVoList", postsVoList);
        result.put("pagination", pagination);
        result.put("searchType", searchType);
        result.put("searchKeyword", searchKeyword);

        return result;
    }

    // 게시글 보기
    public PostsVo read(int id) {
        return postsDao.read(id);
    }

    // 게시글 수정
    public boolean update(PostsVo postsVo) {
        int result = postsDao.update(postsVo);
        return result > 0;
    }

    // 게시글 삭제
    public boolean delete(PostsVo postsVo) {
        int result = postsDao.delete(postsVo.getId());
        return result > 0;
    }

    public boolean delete(int id) {
        int result = postsDao.delete(id);
        return result > 0;
    
    }
    
// ✅ 게시글 삭제 (관리자 또는 작성자)
public boolean deletePost(int postId, String userId, String userRole) {
    PostsVo post = postsDao.read(postId);

    if (post == null) {
        return false;
    }

    // ✅ 관리자는 모든 게시글 삭제 가능
    if ("ROLE_ADMIN".equals(userRole)) {
        return postsDao.delete(postId) > 0;  // 🔹 `int` 값을 `boolean`으로 변환
    }

    // ✅ 일반 사용자는 본인 게시글만 삭제 가능
    if (userId.equals(post.getCreatedBy())) {
        return postsDao.delete(postId) > 0;  // 🔹 `int` 값을 `boolean`으로 변환
    }

    return false;
}


    
}
