<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 실패 메시지 -->
<c:if test="${not empty errorMessage}">
    <div class="row">
        <div class="<c:choose>
            <c:when test='${message_class is not empty}'>${message_class}</c:when>
            <c:otherwise>col-12</c:otherwise>
        </c:choose>">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </div>
</c:if>
<!--// 실패 메시지 -->

<!-- 성공 메시지 -->
<c:if test="${not empty successMessage}">
    <div class="row">
        <div class="<c:choose>
            <c:when test='${message_class is not empty}'>${message_class}</c:when>
            <c:otherwise>col-12</c:otherwise>
        </c:choose>">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </div>
</c:if>
<!--// 성공 메시지 -->
