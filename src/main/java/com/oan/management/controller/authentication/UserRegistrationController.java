package com.oan.management.controller.authentication;

import com.oan.management.model.User;
import com.oan.management.service.user.UserService;
import com.oan.management.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author Oan Stultjens
 * Controller for the registration and validation of the {@link UserRegistrationDto}
 * When an user is registering, the form will pass the data to {@link UserRegistrationDto}, which
 * afterwards will be saved to the database
 */

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Authentication authentication) {
        if (authentication == null) {
            return "registration";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        User existing_username = userService.findByUser(userDto.getUsername());
        if (existing_username != null) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }

        User existing_email = userService.findByEmail(userDto.getEmail());
        if (existing_email != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            return "registration";
        }
        userService.save(userDto);
        return "redirect:/registration?success";
    }

}
