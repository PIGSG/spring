package com.example.spring.users;

import java.util.Map;

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

    // ✅ 일반 사용자는 접근 금지
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 🔥 일반 사용자는 차단됨
    public ModelAndView listUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(required = false) String searchType,
                                  @RequestParam(required = false) String searchKeyword) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> result = usersService.list(page, searchType, searchKeyword);
        mav.addObject("userVoList", result.get("userVoList"));
        mav.addObject("pagination", result.get("pagination"));
        mav.setViewName("user/list");
        return mav;
    }

    // ✅ 사용자 정보 조회 (ADMIN만 가능)
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 🔥 일반 사용자는 접근 불가
    public ModelAndView readGet(@PathVariable("userId") String userId) {
        ModelAndView mav = new ModelAndView();
        UsersVo usersVo = new UsersVo();
        usersVo.setUserId(userId);
        usersVo = usersService.read(usersVo);
        mav.addObject("usersVo", usersVo);
        mav.setViewName("user/read");
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

    
    
}
