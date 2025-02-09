package com.example.spring.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring.users.UserService;
import com.example.spring.users.UsersVo;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);  // SLF4J Logger

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ModelAndView register(@RequestBody UsersVo usersVo, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        try {
            boolean created = userService.create(usersVo);

            if (created) {
                redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다.");
                mav.setViewName("redirect:/auth/login");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다.");
                mav.setViewName("redirect:/auth/register");
            }
        } catch (Exception e) {
            logger.error("회원가입 처리 중 오류가 발생했습니다: ", e);  // 예외 로깅
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다.");
            mav.setViewName("redirect:/auth/register");
        }
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView login(UsersVo usersVo, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
    
        try {
            // null 체크 추가
            if (usersVo == null || usersVo.getUserId() == null || usersVo.getPassword() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 입력되지 않았습니다.");
                mav.setViewName("redirect:/auth/login");
                return mav;
            }
    
            usersVo = authService.login(usersVo);
    
            if (usersVo != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", usersVo);
                session.setAttribute("isLoggedIn", true);
    
                logger.info("User {} logged in successfully.", usersVo.getUserId());  // 로그 기록
    
                mav.setViewName("redirect:/auth/profile");
                return mav;
            }
    
            redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
            mav.setViewName("redirect:/auth/login");
    
        } catch (Exception e) {
            logger.error("Login error occurred for user {}: ", usersVo != null ? usersVo.getUserId() : "Unknown", e);  // 오류 로깅
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 처리 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/auth/login");
        }
    
        return mav;
    }
    

    // 로그아웃
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ModelAndView("redirect:/auth/login");
    }

    // 프로필
    @GetMapping("/profile")
    public ModelAndView profile() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("auth/profile");
        return mav;
    }

    // 프로필 수정
    @PostMapping("/update-profile")
    public ModelAndView updateProfile(UsersVo usersVo, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();

        try {
            boolean updated = userService.update(usersVo);
            if (updated) {
                redirectAttributes.addFlashAttribute("successMessage", "프로필이 수정되었습니다.");
                mav.setViewName("redirect:/auth/profile");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "프로필 수정에 실패했습니다.");
                mav.setViewName("redirect:/auth/update-profile");
            }
        } catch (Exception e) {
            logger.error("Profile update error occurred for user {}: ", usersVo.getUserId(), e);  // 오류 로깅
            redirectAttributes.addFlashAttribute("errorMessage", "프로필 수정 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/auth/update-profile");
        }

        return mav;
    }

    // 비밀번호 수정
    @PostMapping("/update-password")
    public ModelAndView updatePassword(@RequestParam("password") String password,
                                       @RequestParam("password1") String password1,
                                       HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();

        try {
            String userId = (String) request.getSession().getAttribute("userId");
            UsersVo existUsersVo = new UsersVo();
            existUsersVo.setUserId(userId);

            UsersVo existUser = userService.read(existUsersVo);

            if (!existUser.getPassword().equals(password)) {
                redirectAttributes.addFlashAttribute("errorMessage", "기존 비밀번호가 일치하지 않습니다.");
                mav.setViewName("redirect:/auth/update-password");
            } else {
                existUsersVo.setPassword(password1);
                boolean updated = userService.updatePassword(existUsersVo);

                if (updated) {
                    redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 수정되었습니다.");
                    mav.setViewName("redirect:/auth/profile");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 수정에 실패했습니다.");
                    mav.setViewName("redirect:/auth/update-password");
                }
            }
        } catch (Exception e) {
            logger.error("Password update error occurred: ", e);  // 오류 로깅
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 수정 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/auth/update-password");
        }

        return mav;
    }
}
