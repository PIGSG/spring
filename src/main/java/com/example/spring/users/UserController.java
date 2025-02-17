package com.example.spring.users;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
        @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersService usersService;


// ✅ 사용자 정보 조회 (ADMIN만 가능)
@GetMapping("/{userId}")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 🔥 일반 사용자는 접근 불가
public ModelAndView readGet(@PathVariable("userId") String userId) {
    ModelAndView mav = new ModelAndView();

    UsersVo user = new UsersVo();  // ✅ UsersVo 객체 생성
    user.setUserId(userId);
    user = usersService.read(user);

    if (user == null) {
        mav.setViewName("redirect:/user");  // ✅ 사용자가 존재하지 않으면 목록으로 이동
        return mav;
    }

    mav.addObject("user", user);  // ✅ JSP에서 사용할 객체 전달
    mav.setViewName("user/read");  // ✅ JSP 파일명 설정
    return mav;
}



// 사용자 삭제
@PostMapping("/{userId}/delete")
public ModelAndView deleteUser(@PathVariable("userId") String userId,
                               @RequestParam("password") String password,
                               RedirectAttributes redirectAttributes) {
    ModelAndView mav = new ModelAndView();
    
    // 🔹 사용자 정보 조회 (비밀번호 포함)
    UsersVo usersVo = new UsersVo();
    usersVo.setUserId(userId);
    usersVo = usersService.read(usersVo);  // 🔹 비밀번호 가져오는지 확인

    // 🔹 비밀번호가 null인지 체크
    if (usersVo == null || usersVo.getPassword() == null) {
        redirectAttributes.addFlashAttribute("errorMessage", "사용자 정보를 찾을 수 없습니다.");
        mav.setViewName("redirect:/user/");
        return mav;
    }

    // 🔹 비밀번호 검증 (로그 출력 추가)
    System.out.println("입력한 비밀번호: " + password);
    System.out.println("저장된 해시 비밀번호: " + usersVo.getPassword());

    if (!passwordEncoder.matches(password, usersVo.getPassword())) {
        redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
        mav.setViewName("redirect:/user/" + userId);
        return mav;
    }

    // 🔹 비밀번호가 일치하면 삭제
    boolean result = usersService.delete(usersVo, password);
    if (result) {
        redirectAttributes.addFlashAttribute("successMessage", "사용자가 삭제되었습니다.");
        mav.setViewName("redirect:/user/");
    } else {
        redirectAttributes.addFlashAttribute("errorMessage", "사용자 삭제에 실패하였습니다.");
        mav.setViewName("redirect:/user/" + userId);
    }
    return mav;
}

@GetMapping("")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  
public ModelAndView listUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(required = false) String searchType,
                              @RequestParam(required = false) String searchKeyword,
                              HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    String userRole = session != null ? (String) session.getAttribute("role") : null;

    // 🚨 디버깅 로그 추가 (관리자 세션 확인)
    System.out.println("🔍 현재 로그인 사용자 Role: " + userRole);

    // ✅ 관리자가 아닌 경우 접근 제한
    if (!"ROLE_ADMIN".equals(userRole)) {
        System.out.println("🚨 접근 제한: 관리자 권한 없음! 로그인 페이지로 리디렉트됨.");
        return new ModelAndView("redirect:/auth/login?error=auth");
    }

    // ✅ 사용자 목록 조회
    Map<String, Object> result = usersService.list(page, searchType, searchKeyword);
    System.out.println("✅ 사용자 목록 조회 완료 - 총 사용자 수: " + ((result.get("userVoList") != null) ? ((List<?>) result.get("userVoList")).size() : 0));

    // ✅ ModelAndView에 데이터 추가
    ModelAndView mav = new ModelAndView("user/list");
    mav.addObject("userVoList", result.get("userVoList"));  // 🔥 사용자 목록 전달
    mav.addObject("pagination", result.get("pagination"));  // 🔥 페이지네이션 추가
    mav.addObject("searchType", searchType);
    mav.addObject("searchKeyword", searchKeyword);

    return mav;
}



    
    
}
