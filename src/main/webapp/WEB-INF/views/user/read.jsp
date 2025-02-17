<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <!-- ✅ 날짜 포맷용 태그 추가 -->

<!-- ✅ 추가된 include 파일 -->
<%@ include file="../base/top.jsp" %>
<%@ include file="../base/navbar.jsp" %>
<%@ include file="../base/title.jsp" %>
<%@ include file="../base/message.jsp" %>

<div class="container">
    <!-- 메시지 -->
    <jsp:include page="../base/message.jsp" />
    <!--// 메시지 -->

    <h1 class="text-center my-4">사용자 정보</h1>

    <!-- 사용자 보기 -->
    <table class="table table-bordered mb-3">
        <tr>
            <th class="text-center col-md-2">아이디</th>
            <td class="col-md-10">${user.userId}</td>
        </tr>
        <tr>
            <th class="text-center col-md-2">이름</th>
            <td class="col-md-10">${user.username}</td>
        </tr>
        <tr>
            <th class="text-center col-md-2">이메일</th>
            <td class="col-md-10">${user.email}</td>
        </tr>
        <tr>
            <th class="text-center col-md-2">연락처</th>
            <td class="col-md-10">${user.tel}</td>
        </tr>
        <tr>
            <th class="text-center col-md-2">상태</th>
            <td class="col-md-10">
                <c:choose>
                    <c:when test="${user.status == 'active'}">활성</c:when>
                    <c:otherwise>비활성</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <th class="text-center col-md-2">등록일시</th>
            <td class="col-md-10">
                <c:choose>
                    <c:when test="${not empty user.createdAtAsDate}">
                        <fmt:formatDate value="${user.createdAtAsDate}" pattern="yyyy-MM-dd HH:mm" />
                    </c:when>
                    <c:otherwise>-</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <th class="text-center col-md-2">마지막 로그인</th>
            <td class="col-md-10">
                <c:choose>
                    <c:when test="${not empty user.lastLoginAtAsDate}">
                        <fmt:formatDate value="${user.lastLoginAtAsDate}" pattern="yyyy-MM-dd HH:mm" />
                    </c:when>
                    <c:otherwise>-</c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <div>
        <a href="/user" class="btn btn-primary">사용자 목록</a>
        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">사용자 삭제</button>
    </div>
</div>

<!-- 삭제 모달 -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteModalLabel">사용자 삭제</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="/user/${user.userId}/delete" method="POST">
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="password" class="form-label">비밀번호</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                        <div class="form-text text-danger">
                            사용자를 삭제하면 복구할 수 없습니다. 삭제하시려면 사용자 비밀번호를 입력하세요.
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    <button type="submit" class="btn btn-danger">삭제</button>
                </div>
            </form>
        </div>
    </div>
</div>
<!--// 삭제 모달 -->

<!-- ✅ 추가된 include 파일 -->
<%@ include file="../base/script.jsp" %>
<%@ include file="../base/bottom.jsp" %>
