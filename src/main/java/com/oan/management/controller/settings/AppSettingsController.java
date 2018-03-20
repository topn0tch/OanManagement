package com.oan.management.controller.settings;

import com.oan.management.model.User;
import com.oan.management.service.message.MessageService;
import com.oan.management.service.task.TaskService;
import com.oan.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Oan on 1/02/2018.
 * @author Oan Stultjens
 * @since 1/02/2018
 * Controller for the Application Settings in the Settings page
 * Users can change the bahavior of their planner management
 */
@Controller
public class AppSettingsController {
    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    MessageService messageService;

    @GetMapping("/appsettings")
    public String getSettings(Model model, Authentication authentication, HttpServletRequest req) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            userService.updateUserAttributes(userLogged, req);
        }
        return "appsettings";
    }

    @PostMapping("/appsettings")
    public String saveSettings(Model model, Authentication authentication,
                               @RequestParam(value = "motivationText", required = false) String checkbox,
                               @RequestParam(value = "smallCalendar", required = false) String smallCalendar,
                               @RequestParam(value = "todoToCalendar", required = false) String todoToCalendar,
                               @RequestParam(value = "showEmail", required = false) String showEmail) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            if (checkbox == null) {
                userService.setMotivationalTaskMessage(userLogged, false);
            } else {
                userService.setMotivationalTaskMessage(userLogged, true);
            }
            if (smallCalendar == null) {
                userService.setSmallCalendar(userLogged, false);
            } else {
                userService.setSmallCalendar(userLogged, true);
            }
            if (todoToCalendar == null) {
                userService.setTodoToCalendar(userLogged, false);
            } else {
                userService.setTodoToCalendar(userLogged, true);
            }
            if (showEmail == null) {
                userService.setShowEmail(userLogged, false);
            } else {
                userService.setShowEmail(userLogged, true);
            }
        }
        return "redirect:/appsettings?success";
    }
}
