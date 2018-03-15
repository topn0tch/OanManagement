package com.oan.management.controller.social;

import com.oan.management.exception.StorageFileNotFoundException;
import com.oan.management.model.User;
import com.oan.management.service.image.StorageService;
import com.oan.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UploadAvatarController {

    private final StorageService storageService;

    @Autowired
    private UserService userService;

    @Autowired
    public UploadAvatarController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/upload-avatar")
    public String uploadAvatarPage(Model model, Authentication authentication, HttpServletRequest req) {
        User userLogged = userService.findByUser(authentication.getName());
        model.addAttribute("loggedUser", userLogged);
        userService.updateUserAttributes(userLogged, req);
        return "/upload-avatar";
    }

    @PostMapping("/upload-avatar")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Authentication authentication) {
        User user = userService.findByUser(authentication.getName());
        storageService.store(file, user.getId());
        return "redirect:/upload-avatar?success";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}