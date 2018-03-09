package com.oan.management.controller.administration;

import com.oan.management.model.User;
import com.oan.management.repository.UserRepository;
import com.oan.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Oan on 5/02/2018.
 * Controls all administration tools inside the application, which can
 * only be used by users with the ROLE_ADMIN role
 */
@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin")
    public String getAdminPanel(Model model, Authentication authentication, HttpServletRequest req) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            userService.updateUserAttributes(userLogged, req);
        }
        return "admin";
    }

    @GetMapping("/admin/manageusers")
    public String getUserManagement(Model model, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());
        List<User> users = userService.findAll();
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("users", users);
        }
        return "manageusers";
    }

    @GetMapping("/admin/manageusers/{id}")
    public String getUserManagement(Model model, Authentication authentication, @PathVariable Long id) {
        User userLogged = userService.findByUser(authentication.getName());
        List<User> users = userService.findAll();
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            if (id != null) {
                User paramUser = userService.findById(id);
                model.addAttribute("paramUser", paramUser);
                model.addAttribute("currentCountry", paramUser.getCountry());
            }
        }
        return "edituser";
    }

    @PostMapping("/admin/manageusers/{id}")
    public String editUser(Model model, User user, @PathVariable Long id) {
        User paramUser = userService.findById(id);
        userService.editByUser(paramUser, user.getFirstName(), user.getLastName(), user.getCountry(), user.getAge(),
                user.getFacebook(), user.getSkype(), user.getGithub(), user.getEmail(), user.getUsername());
        userRepository.save(paramUser);
        return "redirect:/admin/manageusers/"+paramUser.getId();
    }
}
