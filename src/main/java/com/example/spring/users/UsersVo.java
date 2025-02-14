package com.example.spring.users;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.Data;

@Data
public class UsersVo {
    private String userId;
    private String password;
    private String username; 
    private String tel;
    private String email;

    private LocalDateTime createdAt;  // ✅ String → LocalDateTime 변경
    private LocalDateTime updatedAt;  // ✅ String → LocalDateTime 변경
    private LocalDateTime lastLoginAt;  // ✅ 유지

    private String role;
    private String status;

    // ✅ 생성된 Getter/Setter는 Lombok이 자동으로 생성

    // ✅ JSP에서 `fmt:formatDate`를 활용할 수 있도록 `java.util.Date` 반환 메서드 추가
    public Date getLastLoginAtAsDate() {
        return lastLoginAt != null ? Date.from(lastLoginAt.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public Date getCreatedAtAsDate() {
        return createdAt != null ? Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public Date getUpdatedAtAsDate() {
        return updatedAt != null ? Date.from(updatedAt.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }
}
