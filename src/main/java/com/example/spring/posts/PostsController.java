package com.example.spring.posts;

import java.io.File;
import java.io.IOException;
<<<<<<< HEAD
=======
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
>>>>>>> e6083e0 (Initial commit)
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
=======
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
>>>>>>> e6083e0 (Initial commit)
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

<<<<<<< HEAD
=======
import com.example.spring.users.UsersVo;


>>>>>>> e6083e0 (Initial commit)
@Controller
@RequestMapping("/posts")
public class PostsController {
    
    @Autowired
    PostsService postsService;

    private final String uploadPath = "C:/uploads/posts";
    private static final Logger logger = LoggerFactory.getLogger(PostsController.class); // logger 선언 추가


<<<<<<< HEAD



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
=======
    // 게시글 작성 페이지
    @GetMapping("/create")
    public ModelAndView createPostPage(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String userId = (String) request.getSession().getAttribute("userId");

        if (userId == null) {
            mav.setViewName("redirect:/auth/login");
        } else {
            mav.setViewName("posts/create");
        }
        return mav;
    }
    

    // 게시글 작성 처리
    @PostMapping("/create")
    public ModelAndView createPost(PostsVo postsVo, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        try {
            String userId = (String) request.getSession().getAttribute("userId");
            if (userId == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 게시글을 작성해 주세요.");
                return new ModelAndView("redirect:/auth/login");
            }

            postsVo.setCreatedBy(userId);
            postsVo.setUserId(userId);

            if (postsVo.getUploadFile() != null && !postsVo.getUploadFile().isEmpty()) {
                handleFileUpload(postsVo);
            }

            boolean created = postsService.create(postsVo);
            mav.setViewName(created ? "redirect:/posts" : "redirect:/posts/create");
        } catch (IOException e) {
            logger.error("파일 업로드 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/posts/create");
        } catch (Exception e) {
            logger.error("게시글 등록 중 예상치 못한 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "예상치 못한 오류가 발생했습니다.");
            mav.setViewName("redirect:/posts/create");
        }
        return mav;
    }

    // 파일 업로드 처리
>>>>>>> e6083e0 (Initial commit)
    private void handleFileUpload(PostsVo postsVo) throws IOException {
        MultipartFile uploadFile = postsVo.getUploadFile();
        String originalFileName = uploadFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
<<<<<<< HEAD
    
        // 업로드 디렉토리가 없으면 생성
=======

>>>>>>> e6083e0 (Initial commit)
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
<<<<<<< HEAD
    
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

=======

        File destFile = new File(uploadPath + File.separator + fileName);
        uploadFile.transferTo(destFile);

        postsVo.setFileName(fileName);
        postsVo.setOriginalFileName(originalFileName);
    }

    // 게시글 목록
    @GetMapping({"", "/"})
    public ModelAndView listGet(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(required = false) String searchType,
                                @RequestParam(required = false) String searchKeyword,
                                HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String userId = (String) request.getSession().getAttribute("userId");

        if (userId == null) {
            mav.setViewName("redirect:/auth/login");
            return mav;
        }

        int pageSize = 10;
        Map<String, Object> result = postsService.list(page, pageSize, searchType, searchKeyword);
        mav.addObject("postsVoList", result.get("postsVoList"));
        mav.addObject("pagination", result.get("pagination"));
        mav.setViewName("posts/list");
        return mav;
    }
    
    



    @GetMapping("/{id}")
    public ModelAndView readGet(@PathVariable("id") int id, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
    
        if (userId == null) {
            return new ModelAndView("redirect:/auth/login");
        }
    
        PostsVo postsVo = postsService.read(id);
        if (postsVo == null) {
            return new ModelAndView("redirect:/posts/"); // 존재하지 않는 게시글이라면 목록으로 이동
        }
    
        ModelAndView mav = new ModelAndView("posts/read");
        mav.addObject("postsVo", postsVo);
        mav.addObject("userId", userId); // 현재 로그인한 사용자 ID 추가
        return mav;
    }
    

   // 게시글 삭제
   @PostMapping("/{id}/delete")
   public ModelAndView deletePost(@PathVariable("id") int id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
       String userId = (String) request.getSession().getAttribute("userId");

       if (userId == null) {
           redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 삭제할 수 있습니다.");
           return new ModelAndView("redirect:/auth/login");
       }

       PostsVo postsVo = postsService.read(id);
       if (postsVo == null || !userId.equals(postsVo.getCreatedBy())) {
           redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 게시글만 삭제할 수 있습니다.");
           return new ModelAndView("redirect:/posts/" + id);
       }

       boolean deleted = postsService.delete(id);
       redirectAttributes.addFlashAttribute(deleted ? "successMessage" : "errorMessage",
               deleted ? "게시글이 삭제되었습니다." : "게시글 삭제에 실패했습니다.");
       return new ModelAndView("redirect:/posts/");
   }
>>>>>>> e6083e0 (Initial commit)
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
<<<<<<< HEAD
=======

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") int id, HttpServletRequest request) {
        PostsVo postsVo = postsService.read(id);
        if (postsVo == null || postsVo.getFileName() == null) {
            return ResponseEntity.notFound().build();
        }
    
        Path filePath = Paths.get(uploadPath, postsVo.getFileName());
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }
    
            // 파일 타입 확인 (IOException 예외 처리 추가)
            String contentType;
            try {
                contentType = Files.probeContentType(filePath);
            } catch (IOException e) {
                logger.error("파일 타입을 확인하는 중 오류 발생", e);
                contentType = "application/octet-stream";  // 기본값 설정
            }
    
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + postsVo.getOriginalFileName() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            logger.error("파일 다운로드 오류", e);
            return ResponseEntity.badRequest().build();
        }
    }
    



    
>>>>>>> e6083e0 (Initial commit)
}
