package com.oan.management.controller.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Oan on 24/01/2018.
 * @author Oan Stultjens
 * This controller only contains the GET controller to show the login page
 */

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(Model model, Authentication authentication) {
        if (authentication == null) {
            model.addAttribute("page-title", "Login");
            return "login";
        } else {
            return "redirect:/";
        }

    }
}
