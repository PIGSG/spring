<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ include file="../base/top.jsp" %>
<%@ include file="../base/navbar.jsp" %>
<%@ include file="../base/title.jsp" %>
<%@ include file="../base/message.jsp" %>

<!-- 페이지 내용 -->
<div class="row">
    <div class="col-12">
        <!-- 회원가입 -->
        <form id="registerForm" action="/auth/register" method="POST">
            <div class="card mb-3">
                <div class="card-header">
                    <h5 class="card-title">회원가입</h5>
                </div>
                <div class="card-body">                
                    <div class="mb-3">
                        <label for="userId" class="form-label">아이디</label>
                        <input type="text" class="form-control" id="userId" name="userId" placeholder="아이디" required>
                    </div>                
                    <div class="mb-3">
                        <label for="passwd" class="form-label">비밀번호</label>
                        <input type="password" class="form-control" id="passwd" name="passwd" placeholder="비밀번호" required>
                    </div>
                    <div class="mb-3">
                        <label for="passwd2" class="form-label">비밀번호 확인</label>
                        <input type="password" class="form-control" id="passwd2" name="passwd2" placeholder="비밀번호 확인" required>
                    </div>             
                    <div class="mb-3">
                        <label for="name" class="form-label">이름</label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="이름" required>
                    </div>
                    <div class="mb-3">
                        <label for="tel" class="form-label">전화번호</label>
                        <input type="text" class="form-control" id="tel" name="tel" placeholder="전화번호" required>
                    </div>                
                    <div class="mb-3">
                        <label for="email" class="form-label">이메일</label>
                        <input type="email" class="form-control" id="email" name="email" placeholder="이메일" required>
                    </div>
                </div>
            </div>
            <div>
                <button type="submit" class="btn btn-primary">회원가입</button>
                <a href="/auth/login" class="btn btn-outline-secondary">로그인</a>
            </div>
        </form>
        <!--// 회원가입 -->
    </div>
</div>
<!--// 페이지 내용 -->

<%@ include file="../base/script.jsp" %>

<!-- script -->
<script>
    $(document).ready(function() {        
        // 회원가입 폼 검증
        $('#registerForm').validate({
            rules: {
                userId: {
                    required: true,
                    minlength: 4,
                    maxlength: 20
                },
                passwd: {
                    required: true,
                    minlength: 8,
                    maxlength: 20
                },
                passwd2: {
                    required: true,
                    equalTo: '#passwd'
                },
                name: {
                    required: true,
                    maxlength: 50
                },
                tel: {
                    required: true,
                    maxlength: 20
                },
                email: {
                    required: true,
                    email: true,
                    maxlength: 50
                }
            },
            messages: {
                userId: {
                    required: '아이디를 입력하세요.',
                    minlength: '아이디는 최소 4자 이상이어야 합니다.',
                    maxlength: '아이디는 최대 20자까지 가능합니다.'
                },
                passwd: {
                    required: '비밀번호를 입력하세요.',
                    minlength: '비밀번호는 최소 8자 이상이어야 합니다.',
                    maxlength: '비밀번호는 최대 20자까지 가능합니다.'
                },
                passwd2: {
                    required: '비밀번호 확인을 입력하세요.',
                    equalTo: '비밀번호가 일치하지 않습니다.'
                },
                name: {
                    required: '이름을 입력하세요.',
                    maxlength: '이름은 최대 50자까지 가능합니다.'
                },
                tel: {
                    required: '전화번호를 입력하세요.',
                    maxlength: '전화번호는 최대 20자까지 가능합니다.'
                },
                email: {
                    required: '이메일을 입력하세요.',
                    email: '올바른 이메일 형식이 아닙니다.',
                    maxlength: '이메일은 최대 50자까지 가능합니다.'
                }
            },
            errorClass: 'is-invalid',
            validClass: 'is-valid',
            errorPlacement: function(error, element) {
                error.addClass('invalid-feedback');
                element.closest('.mb-3').append(error);
            },
            submitHandler: function(form) {
                form.submit();
            }
        });
    });
</script>
<!--// script -->

<%@ include file="../base/bottom.jsp" %>
