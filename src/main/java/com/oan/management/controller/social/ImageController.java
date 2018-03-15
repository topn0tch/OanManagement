/*
package com.oan.management.controller.social;

import com.oan.management.model.Image;
import com.oan.management.model.User;
import com.oan.management.service.image.ImageService;
import com.oan.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

*/
/**
 * Created by Oan on 30/01/2018.
 * @author Oan Stultjens
 * Controller for the Upload Avatar page
 *//*

@Controller
public class ImageController {
    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/upload-avatar")
    public String uploadAvatarPage(Model model, Authentication authentication, HttpServletRequest req) {
        User userLogged = userService.findByUser(authentication.getName());
        model.addAttribute("loggedUser", userLogged);
        model.addAttribute("image", new Image());
        userService.updateUserAttributes(userLogged, req);
        return "/upload-avatar";
    }

    @PostMapping("/upload-avatar")
    public String uploadAvatar(Model model, @RequestParam("file") MultipartFile file, Authentication authentication, HttpServletRequest req) {
        User userLogged = userService.findByUser(authentication.getName());
        if (file.getContentType().contains("png")) {
            imageService.uploadImage(file, "/avatar", userLogged);
        } else {
            return "redirect:/upload-avatar?wrongtype";
        }
        return "redirect:/upload-avatar?success";
    }
}
*/
