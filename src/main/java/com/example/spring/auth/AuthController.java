package com.example.spring.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring.users.UsersService;
import com.example.spring.users.UsersVo;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UsersService usersService;
    private final AuthService authService;

    @Autowired
    public AuthController(UsersService usersService, AuthService authService) {
        this.usersService = usersService;
        this.authService = authService;
    }

    // ✅ 게시글 목록 페이지 (로그인 확인)
    @GetMapping("/posts")
    public String viewPosts(HttpServletRequest request) {
        UsersVo loggedInUser = (UsersVo) request.getSession().getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/auth/login"; // 로그인 페이지로 리디렉션
        }
        return "posts/list"; // 게시글 목록 페이지
    }

    // ✅ 회원가입 페이지
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    // ✅ 회원가입 처리
    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute UsersVo usersVo, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        try {
            if (usersService.create(usersVo)) {
                redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다.");
                mav.setViewName("redirect:/auth/login");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다.");
                mav.setViewName("redirect:/auth/register");
            }
        } catch (Exception e) {
            logger.error("회원가입 처리 중 오류 발생: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다.");
            mav.setViewName("redirect:/auth/register");
        }
        return mav;
    }

    // ✅ 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // ✅ 로그인 처리 (세션 저장 및 마지막 로그인 시간 업데이트)
    @PostMapping("/login")
    public ModelAndView login(UsersVo usersVo, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        try {
            usersVo = authService.login(usersVo);
            if (usersVo != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", usersVo);
                session.setAttribute("userId", usersVo.getUserId());
                session.setAttribute("isLoggedIn", true);

                // ✅ 마지막 로그인 시간 업데이트
                usersService.updateLastLogin(usersVo.getUserId());

                mav.setViewName("redirect:/auth/profile");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
                mav.setViewName("redirect:/auth/login");
            }
        } catch (Exception e) {
            logger.error("로그인 오류: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 처리 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/auth/login");
        }
        return mav;
    }

    // ✅ 로그아웃 처리
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ModelAndView("redirect:/auth/login");
    }

    // ✅ 프로필 페이지
    @GetMapping("/profile")
    public ModelAndView profile(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", request.getSession().getAttribute("user"));
        mav.setViewName("auth/profile");
        return mav;
    }

    // ✅ 프로필 수정 페이지
    @GetMapping("/update_profile")
    public String updateProfilePage() {
        return "auth/update_profile";
    }

    // ✅ 프로필 수정 처리
    @PostMapping("/update_profile")
    public ModelAndView updateProfile(@ModelAttribute UsersVo usersVo, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        if (usersService.update(usersVo)) {
            redirectAttributes.addFlashAttribute("successMessage", "프로필이 수정되었습니다.");
            mav.setViewName("redirect:/auth/profile");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "프로필 수정에 실패했습니다.");
            mav.setViewName("redirect:/auth/update_profile");
        }
        return mav;
    }

    // ✅ 비밀번호 변경 페이지
    @GetMapping("/update_password")
    public String updatePasswordPage() {
        return "auth/update_password";
    }

    // ✅ 비밀번호 변경 처리
    @PostMapping("/update_password")
    public ModelAndView updatePassword(
        @RequestParam("password") String password,
        @RequestParam("password1") String newPassword,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        UsersVo usersVo = (UsersVo) request.getSession().getAttribute("user");

        if (!authService.checkPassword(usersVo.getUserId(), password)) {
            redirectAttributes.addFlashAttribute("errorMessage", "기존 비밀번호가 일치하지 않습니다.");
            mav.setViewName("redirect:/auth/update_password");
        } else {
            usersVo.setPassword(newPassword);
            if (usersService.updatePassword(usersVo)) {
                redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 수정되었습니다.");
                mav.setViewName("redirect:/auth/profile");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 수정에 실패했습니다.");
                mav.setViewName("redirect:/auth/update_password");
            }
        }
        return mav;
    }

    // ✅ 비밀번호 초기화
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "auth/reset_password";
    }

    @PostMapping("/reset-password")
    public ModelAndView resetPassword(@ModelAttribute UsersVo usersVo, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        UsersVo user = usersService.read(usersVo);

        if (user != null) {
            String newPassword = authService.resetPassword(usersVo);
            redirectAttributes.addFlashAttribute("successMessage", "초기화된 비밀번호는 " + newPassword + "입니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디를 찾을 수 없습니다.");
        }
        mav.setViewName("redirect:/auth/reset-password");
        return mav;
    }

    // ✅ 회원 탈퇴
    @PostMapping("/delete")
    public ModelAndView delete(@RequestParam("password") String password, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        UsersVo usersVo = (UsersVo) request.getSession().getAttribute("user");

        if (usersService.delete(usersVo, password)) {
            redirectAttributes.addFlashAttribute("successMessage", "회원 탈퇴가 완료되었습니다.");
            mav.setViewName("redirect:/auth/logout");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            mav.setViewName("redirect:/auth/profile");
        }
        return mav;
    }
}
