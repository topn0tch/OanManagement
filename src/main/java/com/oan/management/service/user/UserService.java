package com.oan.management.service.user;

import com.oan.management.model.User;
import com.oan.management.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends UserDetailsService {
    User findByEmail(String email);
    User save(UserRegistrationDto registration);
    User findByUser(String user);
    User findById(Long id);
    List<User> findAll();
    void addBugReport(User userLogged);
    User editByUser(User user, String firstName, String lastName, String country, int age, String facebook, String skype,
                    String github, String twitter, String email, String username);
    void incrementMessagesReceivedStats(User user);
    void incrementMessagesSentStats(User user);
    User setSocialSettings(User user, String facebook, String twitter, String skype, String github);
    User setName(User user, String firstName, String lastName);
    void incrementTasksCreated(User user);
    void decrementTasksCreated(User user);

    void incrementTasksReceived(User user);
    void incrementTasksAssigned(User user);

    void updateUserAvatar(User user, HttpServletRequest req);
    void updateUserAttributes(User user, HttpServletRequest req);

    void incrementTasksCompleted(User user);
    void decrementTasksCompleted(User user);

    User setMotivationalTaskMessage(User user, boolean value);
    User setSmallCalendar(User user, boolean value);
    User setTodoToCalendar(User user, boolean value);
    User setShowEmail(User user, boolean value);
}
