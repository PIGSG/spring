package com.example.spring.posts;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostsController {
    
    @Autowired
    PostsService postsService;

    private final String uploadPath = "C:/uploads/posts";
    private static final Logger logger = LoggerFactory.getLogger(PostsController.class); // logger 선언 추가





    @PostMapping("/create")
    public ModelAndView createPost(PostsVo postsVo, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
    
        try {
            // 파일 업로드 처리
            if (postsVo.getUploadFile() != null && !postsVo.getUploadFile().isEmpty()) {
                handleFileUpload(postsVo);
            }
    
            // 로그인 사용자 ID 확인
            String userId = (String) request.getSession().getAttribute("userId");
    
            // 로그인되지 않은 사용자 처리
            if (userId == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 게시글을 작성해 주세요.");
                mav.setViewName("redirect:/auth/login"); // 로그인 페이지로 리디렉션
                return mav;
            }
    
            // CREATED_BY 필드에 userId 값 설정
            postsVo.setCreatedBy(userId);  // 로그인된 사용자 ID를 CREATED_BY에 설정
            postsVo.setUserId(userId);
    
            // 게시글 저장
            boolean create = postsService.create(postsVo);
    
            if (create) {
                redirectAttributes.addFlashAttribute("successMessage", "게시글이 등록되었습니다.");
                mav.setViewName("redirect:/posts/list");  // 게시글 목록으로 리디렉션
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글 등록에 실패했습니다.");
                mav.setViewName("redirect:/posts/create");  // 게시글 등록 페이지로 리디렉션
            }
        } catch (IOException e) {
            logger.error("IOException occurred during post creation: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/posts/create");
        } catch (Exception e) {
            logger.error("Unexpected error occurred during post creation: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "예상치 못한 오류가 발생했습니다.");
            mav.setViewName("redirect:/posts/create");
        }
    
        return mav;
    }
    
    // 파일 업로드 처리 메서드
    private void handleFileUpload(PostsVo postsVo) throws IOException {
        MultipartFile uploadFile = postsVo.getUploadFile();
        String originalFileName = uploadFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
    
        // 업로드 디렉토리가 없으면 생성
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    
        // 파일 저장
        File destFile = new File(uploadPath + File.separator + fileName);
        uploadFile.transferTo(destFile);
    
        // 파일 정보 설정
        postsVo.setFileName(fileName);
        postsVo.setOriginalFileName(originalFileName);
    }
    
    

    // 게시글 목록
    @GetMapping("/")
    public ModelAndView listGet(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(required = false) String searchType,
        @RequestParam(required = false) String searchKeyword,
        HttpServletRequest request
    ) {
        // 로그인 확인
        String userId = (String) request.getSession().getAttribute("userId");
        if (userId == null) {
            // 로그인되지 않은 사용자일 경우 로그인 페이지로 리디렉션
            ModelAndView mav = new ModelAndView("redirect:/auth/login");
            return mav;
        }

        ModelAndView mav = new ModelAndView();
        int pageSize = 10; // 페이지당 게시글 수
        Map<String, Object> result = postsService.list(page, pageSize, searchType, searchKeyword);
        mav.addObject("postsVoList", result.get("postsVoList"));
        mav.addObject("pagination", result.get("pagination"));
        mav.addObject("searchType", result.get("searchType"));
        mav.addObject("searchKeyword", result.get("searchKeyword"));
        mav.setViewName("posts/list");
        return mav;
    }

    // 게시글 보기
    @GetMapping("/{id}")
    public ModelAndView readGet(@PathVariable("id") int id, HttpServletRequest request) {
        // 로그인 확인
        String userId = (String) request.getSession().getAttribute("userId");
        if (userId == null) {
            // 로그인되지 않은 사용자일 경우 로그인 페이지로 리디렉션
            ModelAndView mav = new ModelAndView("redirect:/auth/login");
            return mav;
        }

        ModelAndView mav = new ModelAndView();
        PostsVo postsVo = postsService.read(id);
        mav.addObject("postsVo", postsVo);
        mav.setViewName("posts/read");
        return mav;
    }

    // 게시글 수정
    @GetMapping("/{id}/update")
    public ModelAndView updateGet(@PathVariable("id") int id, HttpServletRequest request) {
        // 로그인 확인
        String userId = (String) request.getSession().getAttribute("userId");
        if (userId == null) {
            // 로그인되지 않은 사용자일 경우 로그인 페이지로 리디렉션
            ModelAndView mav = new ModelAndView("redirect:/auth/login");
            return mav;
        }

        ModelAndView mav = new ModelAndView();
        PostsVo postsVo = postsService.read(id);
        mav.addObject("postsVo", postsVo);
        mav.setViewName("posts/update");
        return mav;
    }

    @PostMapping("/{id}/update")
    public ModelAndView updatePost(@PathVariable("id") int id, PostsVo postsVo, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        // 로그인 확인
        String userId = (String) request.getSession().getAttribute("userId");
        if (userId == null) {
            // 로그인되지 않은 사용자일 경우 로그인 페이지로 리디렉션
            ModelAndView mav = new ModelAndView("redirect:/auth/login");
            return mav;
        }

        ModelAndView mav = new ModelAndView();
    
        try {
            // 기존 게시글 정보 조회
            PostsVo existingPostsVo = postsService.read(postsVo.getId());
            if (existingPostsVo == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
                mav.setViewName("redirect:/posts/"); 
                return mav;
            }
    
            // 파일 처리
            MultipartFile uploadFile = postsVo.getUploadFile();
            String existingFileName = existingPostsVo.getFileName();
    
            // 기존 파일 삭제 처리
            if (postsVo.isDeleteFile() || (uploadFile != null && !uploadFile.isEmpty())) {
                if (existingFileName != null) {
                    File fileToDelete = new File(uploadPath + File.separator + existingFileName);
                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    }
                    postsVo.setFileName(null);
                    postsVo.setOriginalFileName(null);
                }
            } else {
                postsVo.setFileName(existingFileName);
                postsVo.setOriginalFileName(existingPostsVo.getOriginalFileName());
            }
    
            if (uploadFile != null && !uploadFile.isEmpty()) {
                String originalFileName = uploadFile.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
    
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
    
                File destFile = new File(uploadPath + File.separator + fileName);
                uploadFile.transferTo(destFile);
    
                postsVo.setFileName(fileName);
                postsVo.setOriginalFileName(originalFileName);
            }
    
            boolean updated = postsService.update(postsVo);
            if (updated) {
                redirectAttributes.addFlashAttribute("successMessage", "게시글이 수정되었습니다.");
                mav.setViewName("redirect:/posts/" + id);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글 수정에 실패했습니다.");
                mav.setViewName("redirect:/posts/" + id + "/update");
            }
        } catch (IOException e) {
            logger.error("IOException occurred during post update: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/posts/" + id + "/update");
        } catch (Exception e) {
            logger.error("Unexpected error occurred during post update: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 수정 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/posts/" + id + "/update");
        }
    
        return mav;
    }
}
