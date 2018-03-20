package com.oan.management.controller.settings;

import com.oan.management.model.User;
import com.oan.management.repository.UserRepository;
import com.oan.management.service.user.UserService;
import com.oan.management.utility.NameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Oan on 1/02/2018.
 * Controller for the profile settings from the settings page
 */
@Controller
public class SettingsController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/settings")
    public String getSettings(Model model, Authentication authentication, HttpServletRequest req) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("currentCountry", userLogged.getCountry());
            userService.updateUserAttributes(userLogged, req);
        }
        return "settings";
    }

    @PostMapping("/settings")
    public String setSettings(User user, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            userLogged.setCountry(user.getCountry());
            if (user.getAge() <= 100 && user.getAge() >= 0) {
                userLogged.setAge(user.getAge());
                if (user.getFirstName().length() >= 2 && user.getFirstName().length() <= 20 && user.getLastName().length() > 2 && user.getLastName().length() <= 40)
                    if (user.getFirstName().trim().chars().allMatch(Character::isLetter) && NameValidator.check(user.getLastName())) {
                        userService.setName(userLogged, user.getFirstName(), user.getLastName());
                        if (user.getSkype().trim().length() <= 25 && user.getTwitter().trim().length() <= 25 && user.getFacebook().trim().length() <= 25 && user.getGithub().trim().length() <= 25) {
                            userService.setSocialSettings(userLogged, user.getFacebook(), user.getTwitter(), user.getSkype(), user.getGithub());
                        } else {
                            return "redirect:/settings?error";
                        }
                    } else {
                        return "redirect:/settings?error";
                    }
            } else {
                return "redirect:/settings?error";
            }
        } else {
            userLogged.setAge(0);
            return "redirect:/settings?error";
        }
        userRepository.save(userLogged);
        return "redirect:/settings?success";
    }
}
