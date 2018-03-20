package com.oan.management.controller.task;

import com.oan.management.model.Task;
import com.oan.management.model.User;
import com.oan.management.service.rank.RankService;
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
import javax.validation.Valid;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Oan on 18/01/2018.
 */

@Controller
public class TaskController {
    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RankService rankService;

    public User getLoggedUser(Authentication authentication) {
        return userService.findByUser(authentication.getName());
    }

    @GetMapping("/task-list")
    public String tasklist(HttpServletRequest req, Model model, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        model.addAttribute("task", new Task());
        // Get uncompleted tasks and sort by date
        List<Task> taskList = taskService.findByUserAndCompletedIsFalseAndApprovedIsTrue(userLogged);
        taskList.sort(Comparator.comparing(Task::getTargetDate));
        // get completed tasks and sort by date
        List<Task> completedTasksList = taskService.findByUserAndCompletedIsTrueAndApprovedIsTrue(userLogged);
        completedTasksList.sort(Comparator.comparing(Task::getTargetDate));

        // Check for pending tasks
        List<Task> pendingTasks = taskService.findByUserAndApprovedIsFalse(userLogged);
        model.addAttribute("pendingTasks", pendingTasks);
        model.addAttribute("pendingTasksCount", pendingTasks.size());

        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("tasks", taskList);
            model.addAttribute("completedTasks", completedTasksList);
            userService.updateUserAttributes(userLogged, req);
        }
        String today = new Date(Calendar.getInstance().getTime().getTime()).toString();
        model.addAttribute("today", today);
        return "task-list";
    }

    @GetMapping("/tasks-pending")
    public String getPendingTasksPage(Model model, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        model.addAttribute("loggedUser", userLogged);
        List<Task> pendingTasks = taskService.findByUserAndApprovedIsFalse(userLogged);
        model.addAttribute("pendingTasks", pendingTasks);
        return "tasks-pending";
    }

    @PostMapping("/task-list")
    public String newTask(Model model, @Valid Task task, @RequestParam("targetDate") String date, Authentication authentication) {
        // Get logged in user and his tasks
        User userLogged = getLoggedUser(authentication);
        List<Task> taskList = taskService.findByUser(userLogged);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("tasks", taskList);
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.sql.Date sqlDate = new java.sql.Date(format.parse(date).getTime());
            task.setTargetDate(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        taskService.save(new Task(userLogged, task.getDescription(), task.getTargetDate(), task.isCompleted(), userLogged, true ));
        userService.incrementTasksCreated(userLogged);
        return "redirect:/task-list";
    }

    @GetMapping("/task-assign")
    public String assignTaskPage(Model model, Authentication authentication, Task task, @RequestParam Long id) {
        User userLogged = getLoggedUser(authentication);
        User recepient = userService.findById(id);
        if (recepient != null) {
            if (userLogged.getId() != id) {
                model.addAttribute("recepient",recepient);
                model.addAttribute("loggedUser", userLogged);
                model.addAttribute("task", new Task());
                task.setUser(recepient);
                return "task-assign";
            } else {
                return "redirect:task-list?self";
            }
        } else {
            return "redirect:task-list?usernotfound";
        }
    }

    @PostMapping("/task-assign")
    public String assignTask(Model model, Task task, Authentication authentication, @RequestParam Long id, @RequestParam("targetDate") String date) {
        User userLogged = getLoggedUser(authentication);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.sql.Date sqlDate = new java.sql.Date(format.parse(date).getTime());
            task.setTargetDate(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        User target = userService.findById(id);
        if (target != null) {
            taskService.save(new Task(target, task.getDescription(), task.getTargetDate(), false, userLogged, false ));
            return "redirect:/profile?id="+target.getId()+"&assigned";
        } else {
            return "redirect:task-list?usernotfound";
        }
    }

    @GetMapping("/task-approve")
    public String approveTask(Authentication authentication, @RequestParam Long id, HttpServletRequest req) {
        User userLogged = getLoggedUser(authentication);
        List<Task> pendingTasks = taskService.findByUserAndApprovedIsFalse(userLogged);
        req.setAttribute("pendingTasksCount", pendingTasks.size());
        if (id != null) {
            if (taskService.findByUser(userLogged).contains(taskService.getOne(id))) {
                taskService.approveTask(taskService.findById(id));
                    req.getSession().setAttribute("pendingTasksCount", pendingTasks.size()-1);
                return "redirect:/tasks-pending?approved";
            } else {
                return "redirect:/tasks-pending?notfound";
            }
        } else {
            return "redirect:/tasks-pending?notfound";
        }
    }

    @GetMapping("/task-deny")
    public String denyTask(Authentication authentication, @RequestParam Long id) {
        User userLogged = getLoggedUser(authentication);
        if (id != null) {
            if (taskService.findByUser(userLogged).contains(taskService.getOne(id))) {
                taskService.denyTask(taskService.findById(id));
                return "redirect:/tasks-pending?denied";
            } else {
                return "redirect:/tasks-pending?notfound";
            }
        } else {
            return "redirect:/tasks-pending?notfound";
        }
    }

    @GetMapping("/task-delete")
    public String deleteTask(@RequestParam Long id, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        // Check if it's user's task
        if (taskService.findByUser(getLoggedUser(authentication)).contains(taskService.getOne(id))) {
            taskService.deleteTaskById(id);
            userService.decrementTasksCreated(userLogged);
            if (taskService.findById(id).isCompleted()) {
                userService.decrementTasksCompleted(userLogged);
            }
            return "redirect:/task-list?deleted";
        } else {
            return "redirect:/task-list?notfound";
        }
    }

    @GetMapping("/task-edit")
    public String editTaskOnScreen(Model model, @RequestParam Long id, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        List<Task> taskList = taskService.findByUser(userLogged);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("tasks", taskList);
        }
        // Check if it's user's task
        if (taskService.findByUser(getLoggedUser(authentication)).contains(taskService.getOne(id))) {
            model.addAttribute("task", taskService.getOne(id));
            return "/task-edit";
        } else {
            return "redirect:task-list?notfound";
        }
    }

    @PostMapping("/task-edit")
    public String editTask(Model model, Task task, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        List<Task> taskList = taskService.findByUser(userLogged);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("tasks", taskList);
        }
        taskService.editById(task.getId(),task.getDescription(),task.getTargetDate(), task.isCompleted());
        return "redirect:task-list?updated";
    }

    @GetMapping("/task-complete")
    public String completeTask(@RequestParam Long id, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        taskService.completeTaskById(id);
        // Update statistics and rank
        userService.incrementTasksCompleted(userLogged);
        if (rankService.findByUser(userLogged)==null) {
            rankService.setRank(userLogged, "Newbie", 1);
        } else {
            rankService.checkRank(userLogged);
        }
        return "redirect:/task-list";
    }

    @GetMapping("/task-uncomplete")
    public String uncompleteTask(@RequestParam Long id, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        taskService.uncompleteTaskById(id);
        userService.decrementTasksCompleted(userLogged);
        return "redirect:/task-list";
    }
}
