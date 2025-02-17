<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../base/top.jsp" %>
<%@ include file="../base/navbar.jsp" %>
<%@ include file="../base/title.jsp" %>
<%@ include file="../base/message.jsp" %>

<!-- ✅ 세션에서 로그인된 사용자 ID 및 역할 가져오기 -->
<c:set var="userId" value="${sessionScope.userId}" />
<c:set var="userRole" value="${sessionScope.role}" />  <!-- ✅ 컨트롤러에서 넘겨준 값 사용 -->

<!-- 페이지 내용 -->
<div class="row">
    <div class="col-12">
        <div class="card mb-3">
            <h5 class="card-header text-primary">
                <strong>${postsVo.title}</strong>
            </h5>
            <div class="card-body">  
                <div class="mb-3 text-muted">
                    글쓴이: ${postsVo.username} | 등록일시: 
                    <fmt:formatDate value="${postsVo.createdAt}" pattern="yyyy-MM-dd HH:mm" /> |
                    수정일시: <fmt:formatDate value="${postsVo.updatedAt}" pattern="yyyy-MM-dd HH:mm" />
                </div>

                <!-- 첨부파일 다운로드 (파일이 있을 경우에만) -->
                <c:if test="${not empty postsVo.fileName}">
                    <div class="mb-3">
                        첨부파일: <a href="/posts/${postsVo.id}/download" class="btn btn-outline-primary">${postsVo.originalFileName}</a>
                    </div>
                </c:if>

                <div class="mb-3">
                    ${postsVo.content}
                </div>
            </div>
        </div>

        <!-- 버튼 영역 -->
        <div>
            <a href="/posts/" class="btn btn-primary">목록</a>

            <!-- ✅ 작성자 또는 관리자만 수정 & 삭제 가능 -->
            <c:if test="${userId eq postsVo.createdBy || userRole eq 'ROLE_ADMIN'}">
                <a href="/posts/${postsVo.id}/update" class="btn btn-warning">수정</a>
                <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">삭제</button>
            </c:if>
        </div>
    </div>
</div>

<!-- ✅ 삭제 모달 (작성자 또는 관리자만 보이도록 설정) -->
<c:if test="${userId eq postsVo.createdBy || userRole eq 'ROLE_ADMIN'}">
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm" action="/posts/${postsVo.id}/delete" method="POST">
                <div class="modal-header">
                    <h1 class="modal-title fs-5 text-danger" id="deleteModalLabel">
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
</c:if>

<%@ include file="../base/script.jsp" %>
