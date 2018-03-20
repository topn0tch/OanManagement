package com.oan.management.controller.social;

import com.oan.management.model.Message;
import com.oan.management.model.User;
import com.oan.management.service.message.MessageService;
import com.oan.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Oan on 25/01/2018.
 * @author Oan Stultjens
 * Controller for the Messaging System
 */

@Controller
public class MessageController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    public User getLoggedUser(Authentication authentication) {
        return userService.findByUser(authentication.getName());
    }

    @GetMapping("/messages")
    public String getInbox(HttpServletRequest req, Model model, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        List<Message> messages = messageService.getMessagesByUser(userLogged);
        // Sort by id, last id's last (newest messages on top)
        messages.sort(Comparator.comparing(Message::getId).reversed());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("messages", messages);
            userService.updateUserAttributes(userLogged, req);
        }
        return "messages";
    }

    @GetMapping("/message")
    public String openMessage(HttpServletRequest req, Model model, Authentication authentication, @RequestParam Long id) {
        User userLogged = getLoggedUser(authentication);
        List<Message> unreadMessages = messageService.findByReceiverAndOpenedIs(userLogged, 0);
        // Get username and messages
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            if (unreadMessages.size() >= 1) {
                req.getSession().setAttribute("unreadMessages", unreadMessages.size()-1);
            }
        }
        if (id != null) {
            Message msg = messageService.getMessageById(id);
            model.addAttribute("message", msg);
            msg.setOpened(1);
            messageService.save(msg);
            return "message";
        } else {
            return "redirect:/messages";
        }
    }

    @GetMapping("/message-delete")
    public String deleteMessage(Authentication authentication, @RequestParam Long id) {
        User userLogged = getLoggedUser(authentication);
        List<Message> messages = messageService.getMessagesByUser(userLogged);
        if (messages.contains(messageService.getMessageById(id))) {
            messageService.deleteMessageById(id);
            return "redirect:/messages";
        } else {
            return "redirect:/messages?error";
        }
    }

    @GetMapping("/message-new")
    public String newMessage(Model model, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("message", new Message());
        }
        return "message-new";
    }

    @PostMapping("/message-new")
    public String sendMessage(Model model, Message message, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
        }
        User receiver = userService.findByUser(message.getReceiver().getUsername());
        if (message.getReceiver().getId() != userLogged.getId()) {
            if (receiver != null) {
                if (!(message.getSubject().isEmpty()) && !(message.getMessageText().isEmpty())) {
                    messageService.save(new Message(message.getSubject(), message.getMessageText(), new Date(Calendar.getInstance().getTime().getTime()), userLogged, receiver));
                    // Update statistics
                    userService.incrementMessagesSentStats(userLogged);
                    userService.incrementMessagesReceivedStats(receiver);
                    return "redirect:/messages?success";
                } else {
                    return "redirect:/message-new?emptytext";
                }
            } else {
                return "redirect:/message-new?notfound";
            }
        } else {
            return "redirect:/message-new?self";
        }
    }


    @GetMapping("/message-to")
    public String reply(Model model, Authentication authentication, Message message, @RequestParam Long id) {
        User userLogged = getLoggedUser(authentication);
        User recepient = userService.findById(id);
        if (recepient != null) {
            if (userLogged.getId() != id) {
                model.addAttribute("recepient",recepient);
                if (userLogged != null) {
                    model.addAttribute("loggedUser", userLogged);
                    model.addAttribute("message", new Message());
                }
                message.setReceiver(recepient);
                return "message-to";
            } else {
                return "redirect:messages?self";
            }
        } else {
            return "redirect:messages?erroruser";
        }
    }

    @PostMapping("/message-to")
    public String replyPost(Model model, Message message, Authentication authentication, @RequestParam Long id) {
        User userLogged = getLoggedUser(authentication);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
        }
        User target = userService.findById(id);
        if (target != null) {
            messageService.save(new Message(message.getSubject(), message.getMessageText(), new Date(Calendar.getInstance().getTime().getTime()), userLogged, target));
            // Update statistics
            userService.incrementMessagesSentStats(userLogged);
            userService.incrementMessagesReceivedStats(target);
            return "redirect:/messages?success";
        } else {
            return "redirect:/message-to?error";
        }
    }

    @GetMapping("message-unread")
    public String unreadMessage(@RequestParam Long id) {
        Message msg = messageService.getMessageById(id);
        msg.setOpened(0);
        messageService.save(msg);
        return "redirect:/messages";
    }
}