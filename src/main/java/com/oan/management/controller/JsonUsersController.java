package com.oan.management.controller;

import com.oan.management.model.User;
import com.oan.management.service.message.MessageService;
import com.oan.management.service.user.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oan on 16/02/2018.
 * This is the API for the website to get (specific) user data
 */
@RestController
public class JsonUsersController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/api/users")
    public List<User> getUsers() {
        List<User> users = userService.findAll();
        return users;
    }

    @GetMapping("/api/users/usernames")
    public List<String> getUsernames() {
        List<User> users = userService.findAll();
        ArrayList<String> usernames = new ArrayList<String>();
        for (User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    @GetMapping("/api/users/id/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/api/users/id/{id}/username")
    public String getUsernameById(@PathVariable Long id) {
        return userService.findById(id).getUsername();
    }

    @GetMapping("/api/users/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findByUser(username);
    }

    @GetMapping("/api/user/newmessages")
    public int getUserNotifications(Authentication authentication) {
        if (authentication != null) {
            User userLogged = userService.findByUser(authentication.getName());
            return messageService.findByReceiverAndOpenedIs(userLogged, 0).size();
        } else {
            return 0;
        }
    }

    @GetMapping(value = "/api/avatar/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getUserAvatar(@PathVariable Long id) throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/img/avatar/"+id+".png");
        if (in != null) {
            return IOUtils.toByteArray(in);
        } else {
            in = getClass().getResourceAsStream("/static/img/avatar/0.png");
            return IOUtils.toByteArray(in);
        }

    }
}
