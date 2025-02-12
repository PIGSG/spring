<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<<<<<<< HEAD
=======
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
>>>>>>> e6083e0 (Initial commit)

<!-- 네비게이션 -->
<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
    <div class="container">
        <a class="navbar-brand" href="#">스프링 MVC 프로젝트</a>
        <button class="navbar-toggler collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="navbar-collapse collapse" id="navbarCollapse">
            <ul class="navbar-nav me-auto mb-2 mb-md-0">
                <li class="nav-item">
<<<<<<< HEAD
                    <a class="nav-link" href="/auth/posts">게시글</a>
=======
                    <a class="nav-link" href="/posts">게시글</a>
>>>>>>> e6083e0 (Initial commit)
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/auth/profile">사용자</a>
                </li>
            </ul>
            <div class="d-flex">
                <ul class="navbar-nav me-auto mb-2 mb-md-0">
<<<<<<< HEAD
                    <li class="nav-item">
                        <a class="nav-link" href="#">로그인</a>
                    </li>
=======
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <!-- 로그인 된 상태일 경우: 로그아웃 버튼 -->
                            <li class="nav-item">
                                <a class="nav-link" href="/auth/logout">로그아웃</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <!-- 로그인 안 된 상태일 경우: 로그인 버튼 -->
                            <li class="nav-item">
                                <a class="nav-link" href="/auth/login">로그인</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
>>>>>>> e6083e0 (Initial commit)
                </ul>
            </div>
        </div>
    </div>
</nav>
<<<<<<< HEAD
<!--// 네비게이션 -->
=======
<!--// 네비게이션 -->
>>>>>>> e6083e0 (Initial commit)
