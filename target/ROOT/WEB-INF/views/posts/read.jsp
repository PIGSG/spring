<<<<<<< HEAD
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
=======
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
>>>>>>> e6083e0 (Initial commit)

<%@ include file="../base/top.jsp" %>
<%@ include file="../base/navbar.jsp" %>
<%@ include file="../base/title.jsp" %>
<%@ include file="../base/message.jsp" %>

<<<<<<< HEAD
<!-- 페이지 내용 -->
<div class="row">
    <div class="col-12">
        <!-- 게시글 보기 -->
=======
<!-- 세션에서 로그인된 사용자 가져오기 -->
<%
    String userId = (String) session.getAttribute("userId");
    String postOwner = request.getAttribute("postsVo") != null ? ((com.example.spring.posts.PostsVo) request.getAttribute("postsVo")).getCreatedBy() : "";
%>

<!-- 페이지 내용 -->
<div class="row">
    <div class="col-12">
>>>>>>> e6083e0 (Initial commit)
        <div class="card mb-3">
            <h5 class="card-header text-primary">
                <strong>${postsVo.title}</strong>
            </h5>
            <div class="card-body">  
                <div class="mb-3 text-muted">
<<<<<<< HEAD
                    글쓴이: ${postsVo.username} | 등록일시: ${postsVo.createdAt.substring(0, 16)} | 수정일시: ${postsVo.createdAt.substring(0, 16)}
                </div>
                <div class="mb-3">
                    첨부파일: <a href="/posts/${postsVo.id}/download" class="btn btn-outline-primary">${postsVo.originalFileName}</a>
                </div>
=======
                    글쓴이: ${postsVo.username} | 등록일시: ${postsVo.createdAt} | 수정일시: ${postsVo.updatedAt}
                </div>

                <!-- 첨부파일 다운로드 (파일이 있을 경우에만) -->
                <c:if test="${not empty postsVo.fileName}">
                    <div class="mb-3">
                        첨부파일: <a href="/posts/${postsVo.id}/download" class="btn btn-outline-primary">${postsVo.originalFileName}</a>
                    </div>
                </c:if>

>>>>>>> e6083e0 (Initial commit)
                <div class="mb-3">
                    ${postsVo.content}
                </div>
            </div>
        </div>
<<<<<<< HEAD
        <div>
            <a href="/posts/" class="btn btn-primary">목록</a>
            <a href="/posts/${postsVo.id}/update" class="btn btn-warning">수정</a>
            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">삭제</button>
        </div>
        <!--// 게시글 보기 -->
    </div>
</div>
<!--// 페이지 내용 -->

<!-- 삭제 모달 -->
=======

        <!-- 버튼 영역 -->
        <div>
            <a href="/posts/" class="btn btn-primary">목록</a>

            <!-- 작성자만 수정 & 삭제 가능 -->
            <c:if test="${userId eq postsVo.createdBy}">
                <a href="/posts/${postsVo.id}/update" class="btn btn-warning">수정</a>
                <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">삭제</button>
            </c:if>
        </div>
    </div>
</div>

<!-- 삭제 모달 -->
<c:if test="${userId eq postsVo.createdBy}">
>>>>>>> e6083e0 (Initial commit)
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm" action="/posts/${postsVo.id}/delete" method="POST">
                <div class="modal-header">
                    <h1 class="modal-title fs-5 text-danger" id="deleteModalModalLabel">
                        <strong>게시글 삭제</strong>
                    </h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <p class="text-danger">삭제된 데이터는 복구할 수 없습니다.</p>
                        <p>정말 삭제하시겠습니까?</p>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-outline-danger">삭제</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                </div>
            </form>
        </div>
    </div>
</div>
<<<<<<< HEAD
<!-- 삭제 모달 -->

<%@ include file="../base/script.jsp" %>

<!-- script -->
<script>
    /* 자바스크립트 */
</script>
<!--// script -->

<%@ include file="../base/bottom.jsp" %>
=======
</c:if>

<%@ include file="../base/script.jsp" %>
>>>>>>> e6083e0 (Initial commit)
