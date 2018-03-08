package com.oan.management.service.task;

import com.oan.management.model.Message;
import com.oan.management.model.Task;
import com.oan.management.model.User;
import com.oan.management.repository.TaskRepository;
import com.oan.management.service.message.MessageService;
import com.oan.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Oan on 24/01/2018.
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public List<Task> findByUser(User user) {
        return taskRepository.findByUser(user);
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task getById(Long id) {
        return taskRepository.getById(id);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.delete(taskRepository.findById(id));
    }

    @Override
    public Task completeTaskById(Long id) {
        Task task = taskRepository.findById(id);
        task.setCompleted(true);
        return taskRepository.save(task);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task getOne(Long id) {
        return taskRepository.getById(id);
    }

    @Override
    public Task editById(Long id, String desc, Date date, boolean completed) {
        Task task = taskRepository.getById(id);
        task.setDescription(desc);
        task.setTargetDate(date);
        task.setCompleted(task.isCompleted());
        return taskRepository.save(task);
    }

    @Override
    public List<Task> findByUserAndCompletedIsFalse(User user) {
        return taskRepository.findByUserAndCompletedIsFalse(user);
    }

    @Override
    public List<Task> findByUserAndCompletedIsTrue(User user) {
        return taskRepository.findByUserAndCompletedIsTrue(user);
    }

    /**
     * Returns a String of a motivational text, according to the amount of tasks to complete for the User
     * @param taskList List of Task's
     * @param user {@link User}
     * @return String of motivation text
     */
    @Override
    public String getMotivationalMessage(List<Task> taskList, User user) {

        if (taskList.size() == 0 && taskRepository.findByUserAndCompletedIsTrue(user).size() == 0) {
           return "Hmm... What about giving yourself some tasks?";
        } else if (taskList.size() == 0 && taskRepository.findByUserAndCompletedIsTrue(user).size() > 0) {
            return "Good job! You've completed all your tasks! Be proud about yourself.";
        } else if (taskList.size() == 1) {
            return "What, only 1 task? You should finish your work!";
        } else if (taskList.size() > 1 && taskList.size() <= 5){
            return "You only have "+taskList.size()+" tasks... That's not that much. Go complete them!";
        } else if (taskList.size() > 5 && taskList.size() <= 10){
            return "You've got some work there, "+taskList.size()+" tasks to complete. Try to complete as much as possible today.";
        } else if (taskList.size() > 10 ){
            return taskList.size()+" tasks! Let's see how many you could complete today! Proof yourself!";
        } else {
            return "Error";
        }
    }

    @Override
    public List<Task> findByUserAndCompletedIsFalseAndApprovedIsTrue(User user) {
        return taskRepository.findByUserAndCompletedIsFalseAndApprovedIsTrue(user);
    }

    @Override
    public List<Task> findByUserAndCompletedIsTrueAndApprovedIsTrue(User user) {
        return taskRepository.findByUserAndCompletedIsTrueAndApprovedIsTrue(user);
    }

    @Override
    public List<Task> findByUserAndApprovedIsFalse(User user) {
        return taskRepository.findByUserAndApprovedIsFalse(user);
    }

    @Override
    public void approveTask(Task task) {
        task.setApproved(true);
        // Increment stats for assign/received
        User user = userService.findById(task.getUser().getId());
        userService.incrementTasksReceived(user);
        User creatorUser = userService.findById(task.getCreator().getId());
        userService.incrementTasksAssigned(creatorUser);
        // Increment the user (who got the task)'s tasksCreated
        userService.incrementTasksCreated(user);
        taskRepository.save(task);
    }

    @Override
    public void denyTask(Task task) {
        deleteTaskById(task.getId());
        Message notifyMessage = new Message();
        notifyMessage.setReceiver(task.getCreator());
        notifyMessage.setSender(task.getUser());
        notifyMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        notifyMessage.setOpened(0);
        notifyMessage.setSubject("Your assigned task to "+task.getUser().getUsername()+" has been denied.");
        notifyMessage.setMessageText("<p>Hello "+task.getCreator().getUsername() + ",</p><br/>You have assigned the following task to me: <blockquote>"+task.getDescription()+
                "</blockquote><p>I hereby wish to inform you that I have denied your task.</p><small><em>This is an automated message and not written by the user self.</em></small>");
        messageService.save(notifyMessage);
    }

    @Override
    public void updateAttributes(User user, HttpServletRequest req) {
        List<Task> taskList = taskRepository.findByUserAndCompletedIsFalseAndApprovedIsTrue(user);
        req.getSession().setAttribute("tasksLeft", taskList.size());
        String motivationMessage = getMotivationalMessage(taskList, user);
        req.getSession().setAttribute("taskMotivation", motivationMessage);
        List<Task> pendingTasks = findByUserAndApprovedIsFalse(user);
        req.getSession().setAttribute("pendingTasksCount", pendingTasks.size());
    }

    @Override
    public Task uncompleteTaskById(Long id) {
        Task task = taskRepository.findById(id);
        task.setCompleted(false);
        return taskRepository.save(task);
    }
}
