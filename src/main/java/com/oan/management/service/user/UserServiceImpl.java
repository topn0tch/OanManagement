package com.oan.management.service.user;

import com.oan.management.model.Role;
import com.oan.management.model.User;
import com.oan.management.repository.UserRepository;
import com.oan.management.service.image.ImageService;
import com.oan.management.service.message.MessageService;
import com.oan.management.service.task.TaskService;
import com.oan.management.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oan Stultjens
 * Implementation for the {@link UserService}
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Registering an user by the {@link UserRegistrationDto}
     * @param registration {@link UserRegistrationDto}
     * @return User
     */
    public User save(UserRegistrationDto registration) {
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        user.setRegistrationDate(new Date(Calendar.getInstance().getTime().getTime()));
        return userRepository.save(user);
    }

    @Override
    public User findByUser(String user) {
        return userRepository.findByUsername(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Increments the value of the specified User's bugsReported value
     * @param userLogged
     */
    @Override
    public void addBugReport(User userLogged) {
        userLogged.setBugsReported(userLogged.getBugsReported()+1);
        userRepository.save(userLogged);
    }

    @Override
    public User editByUser(User user, String firstName, String lastName, String country, int age, String facebook, String skype, String github, String twitter, String email, String username) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCountry(country);
        user.setAge(age);
        user.setFacebook(facebook);
        user.setSkype(skype);
        user.setGithub(github);
        user.setTwitter(twitter);
        user.setEmail(email);
        user.setUsername(username);
        return userRepository.save(user);
    }

    /**
     * Increment messagesReceivedStats from {@link User}
     * @param user User
     */
    @Override
    public void incrementMessagesReceivedStats(User user) {
        user.setMessagesReceived(user.getMessagesReceived()+1);
        userRepository.save(user);
    }

    /**
     * Increment incrementMessagesSentStats from {@link User}
     * @param user User
     */
    @Override
    public void incrementMessagesSentStats(User user) {
        user.setMessagesSent(user.getMessagesSent()+1);
        userRepository.save(user);
    }

    @Override
    public User setSocialSettings(User user, String facebook, String twitter, String skype, String github) {
        user.setFacebook(facebook);
        user.setTwitter(twitter);
        user.setSkype(skype);
        user.setGithub(github);
        return userRepository.save(user);
    }

    @Override
    public User setName(User user, String firstName, String lastName) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    @Override
    public void incrementTasksCreated(User user) {
        user.setTasksMade(user.getTasksMade()+1);
        userRepository.save(user);
    }

    @Override
    public void decrementTasksCreated(User user) {
        user.setTasksMade(user.getTasksMade()-1);
        userRepository.save(user);
    }

    @Override
    public void incrementTasksReceived(User user) {
        user.setTasksReceived(user.getTasksReceived()+1);
        userRepository.save(user);
    }

    @Override
    public void incrementTasksAssigned(User user) {
        user.setTasksAssigned(user.getTasksAssigned()+1);
        userRepository.save(user);
    }

    @Override
    public void updateUserAttributes(User user, HttpServletRequest req) {
        taskService.updateAttributes(user, req);
        messageService.updateAttributes(user, req);
    }

    @Override
    public void incrementTasksCompleted(User user) {
        user.setTasksCompleted(user.getTasksCompleted()+1);
        userRepository.save(user);
    }

    @Override
    public void decrementTasksCompleted(User user) {
        user.setTasksCompleted(user.getTasksCompleted()-1);
        userRepository.save(user);
    }

    @Override
    public User setMotivationalTaskMessage(User user, boolean value) {
        user.setMotivationalTaskMessage(value);
        return userRepository.save(user);
    }

    @Override
    public User setSmallCalendar(User user, boolean value) {
        user.setSmallCalendar(value);
        return userRepository.save(user);
    }

    @Override
    public User setTodoToCalendar(User user, boolean value) {
        user.setTodoToCalendar(value);
        return userRepository.save(user);
    }

    @Override
    public User setShowEmail(User user, boolean value) {
        user.setShowEmail(value);
        return userRepository.save(user);
    }

    /**
     * This method is used to login with an username and password
     * If the login is succesful, the authorities will be granted (mapped from a Collection) to the {@link User}
     * @param username String of the username
     * @return UserDetails from Spring Security
     * @throws UsernameNotFoundException When username hasn't been found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    /**
     * This method extends {@link GrantedAuthority} from SpringSecurity
     * It maps the roles of an user as authorities
     * @param roles Collection of Role's
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
