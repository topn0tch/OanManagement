package com.oan.management.controller.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oan.management.config.CustomSettings;
import com.oan.management.model.Event;
import com.oan.management.model.Task;
import com.oan.management.model.User;
import com.oan.management.service.calendar.EventService;
import com.oan.management.service.task.TaskService;
import com.oan.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oan on 26/01/2018.
 * @author Oan Stultjens
 * RestController for the Events, also maps the To-Do (task)'s to the calendar
 */

@RestController
@RequestMapping("/api/event")
public class RestWebController {
    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @GetMapping("/all")
    public String getEvents(Authentication authentication) {
        String jsonMessage = null;
        try {
            User userLogged = userService.findByUser(authentication.getName());
            List<Event> events = eventService.findAllByUser(userLogged);
            List<Task> tasks = taskService.findByUserAndCompletedIsFalseAndApprovedIsTrue(userLogged);

            if (userLogged.isTodoToCalendar()) {
                for (Task task : tasks) {
                    events.add(new Event("To-do: "+task.getDescription(), "Task from: " + task.getCreator().getFirstName() + " " + task.getCreator().getLastName(), task.getTargetDate().toString(), task.getTargetDate().toString(), userLogged, CustomSettings.EVENT_TODO_COLOUR, CustomSettings.EVENT_TODO_COLOUR, false));
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            jsonMessage = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return jsonMessage;
    }
}
