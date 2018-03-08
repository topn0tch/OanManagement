package com.oan.management.controller.calendar;

import com.oan.management.model.Event;
import com.oan.management.model.User;
import com.oan.management.service.calendar.EventService;
import com.oan.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Oan on 18/01/2018.
 * @author Oan Stultjens
 * Controller for the Calendar
 */

@Controller
public class CalendarController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @GetMapping("/calendar")
    public String calendar(HttpServletRequest req, Model model, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("event", new Event());
            userService.updateUserAttributes(userLogged, req);
        }
        return "calendar";
    }

    /**
     * This controller is used for adding new events
     * @param authentication
     * @param start
     * @param end
     * @param title
     * @param description
     * @param colour
     * @return String
     */
    @GetMapping("/calendar-addevent")
    public String calendarAddEvent(Authentication authentication, @RequestParam String start, @RequestParam String end, @RequestParam String title, @RequestParam String description, @RequestParam String colour) {
        User userLogged = userService.findByUser(authentication.getName());

        if (userLogged != null) {
            if (title.length() > 0) {
                eventService.save(new Event(title, description, start, end, userLogged, colour, colour, true));
                return "redirect:/calendar";
            } else {
                return "redirect:/calendar?error";
            }
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/calendar-delete")
    public String deleteEvent(Authentication authentication, @RequestParam Long id) {
        User userLogged = userService.findByUser(authentication.getName());
        eventService.delete(eventService.findById(id));
        return "redirect:/calendar";
    }

    /**
     * This controller is the simple controller for dragging and dropping events, only title and start/end are passed
     * @param authentication
     * @param title
     * @param start
     * @param end
     * @param id
     * @return String
     */
    @GetMapping("/calendar-update")
    public String updateEvent(Authentication authentication, @RequestParam String title, @RequestParam String start, @RequestParam String end, @RequestParam Long id) {
        User userLogged = userService.findByUser(authentication.getName());
        eventService.editById(id, title, start, end);
        return "redirect:/calendar";
    }

    /**
     * This controller updates an event including it's colour, description etc
     * @param authentication
     * @param title
     * @param start
     * @param end
     * @param id
     * @param colour
     * @param desc
     * @return String
     */
    @GetMapping("/calendar-updateevent")
    public String updateEventNew(Authentication authentication, @RequestParam String title, @RequestParam String start, @RequestParam String end, @RequestParam Long id, @RequestParam String colour, @RequestParam String desc) {
        User userLogged = userService.findByUser(authentication.getName());
        Event event = eventService.findById(id);
        if (title.length() > 0) {
            eventService.editEventAndColour(event, title, desc, colour,  colour);
        } else {
            eventService.editEventAndColour(event, "Event", desc, colour,  colour);
        }
        return "redirect:/calendar";
    }
}
