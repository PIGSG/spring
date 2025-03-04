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

    <!-- 검색 -->
    <div class="mb-3 col-md-4">
        <form method="get" action="/user">
            <div class="input-group">
                <select name="searchType" class="form-select">
                    <option value="all" ${searchType == 'all' ? 'selected' : ''}>전체</option>
                    <option value="userId" ${searchType == 'userId' ? 'selected' : ''}>아이디</option>
                    <option value="username" ${searchType == 'username' ? 'selected' : ''}>이름</option>
                    <option value="tel" ${searchType == 'tel' ? 'selected' : ''}>연락처</option>
                    <option value="email" ${searchType == 'email' ? 'selected' : ''}>이메일</option>
                </select>
                <input type="text" name="searchKeyword" class="form-control" value="${searchKeyword}" placeholder="검색어를 입력하세요">
                <button type="submit" class="btn btn-primary">검색</button>
                <c:if test="${searchType != null}">
                    <a href="/user" class="btn btn-danger">취소</a>
                </c:if>
            </div>
        </form>
    </div>
    <!--// 검색 -->

    <!-- 사용자 목록 -->
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead class="table-light">
                <tr>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>연락처</th>
                    <th>이메일</th>
                    <th>상태</th>
                    <th>마지막 로그인</th>
                    <th>가입일</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userVoList}">
                    <tr>
                        <td><a href="/user/${user.userId}">${user.userId}</a></td>
                        <td>${user.username}</td>
                        <td>${user.tel}</td>
                        <td>${user.email}</td>
                        <td>
                            <c:choose>
                                <c:when test="${user.status == 'active'}">활성</c:when>
                                <c:otherwise>비활성</c:otherwise>
                            </c:choose>
                        </td>
                        <!-- ✅ 날짜 변환하여 출력 -->
                        <td>
                            <c:choose>
                                <c:when test="${not empty user.lastLoginAtAsDate}">
                                    <fmt:formatDate value="${user.lastLoginAtAsDate}" pattern="yyyy-MM-dd HH:mm" />
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <fmt:formatDate value="${user.createdAtAsDate}" pattern="yyyy-MM-dd HH:mm" />
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <!--// 사용자 목록 -->

    <!-- 페이지네이션 -->
    <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <c:if test="${pagination.page > 1}">
                <li class="page-item">
                    <a class="page-link" href="/user/?page=1">처음</a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="/user/?page=${pagination.page - 1}">이전</a>
                </li>
            </c:if>

            <c:forEach begin="${pagination.startPage}" end="${pagination.endPage}" var="pageNum">
                <li class="page-item ${pagination.page == pageNum ? 'active' : ''}">
                    <a class="page-link" href="/user/?page=${pageNum}">${pageNum}</a>
                </li>
            </c:forEach>

            <c:if test="${pagination.page < pagination.totalPages}">
                <li class="page-item">
                    <a class="page-link" href="/user/?page=${pagination.page + 1}">다음</a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="/user/?page=${pagination.totalPages}">마지막</a>
                </li>
            </c:if>
        </ul>
    </nav>
    <!--// 페이지네이션 -->

</div>

<!-- ✅ 추가된 include 파일 -->
<%@ include file="../base/script.jsp" %>
<%@ include file="../base/bottom.jsp" %>
