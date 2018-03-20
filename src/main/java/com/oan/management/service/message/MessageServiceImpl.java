package com.oan.management.service.message;

import com.oan.management.model.Bug;
import com.oan.management.model.Message;
import com.oan.management.model.User;
import com.oan.management.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Oan on 25/01/2018.
 */

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Override
    public List<Message> getMessagesByUser(User user) {
        return messageRepository.getAllByReceiver(user);
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.getMessageById(id);
    }

    @Override
    public void deleteMessageById(Long id) {
        messageRepository.delete(messageRepository.getMessageById(id));
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    /**
     * Get a list of unread messages
     * @param user
     * @return
     */
    @Override
    public List<Message> getAllByReceiverAndOpenedIsFalse(User user) {
        return messageRepository.getAllByReceiverAndOpenedIsFalse(user);
    }

    @Override
    public List<Message> findByReceiverAndOpenedIs(User user, int read) {
        return messageRepository.findByReceiverAndOpenedIs(user, read);
    }

    /**
     * Notifies the reporter of a bug with a {@link Message}
     * @param sender {@link User}
     * @param receiver {@link User}
     * @param bug {@link Bug}
     * @return Message
     */
    @Override
    public Message bugNotifyMessage(User sender, User receiver, Bug bug) {
        Message notifyMessage = new Message();
        notifyMessage.setSender(sender);
        notifyMessage.setReceiver(receiver);
        notifyMessage.setSubject("Your reported bug #"+bug.getId()+" has been fixed.");
        notifyMessage.setMessageText("<p>Hello "+bug.getUser().getFirstName() + ",</p><br/>You have reported the following bug: <blockquote>"+bug.getDescription()+"<footer>"+bug.getUser().getUsername()+" on "+bug.getDate().toString()+"</footer>"+
                "</blockquote><p>This bug has been fixed. We thank you for reporting it to us!</p><br/>Regards");
        notifyMessage.setOpened(0);
        notifyMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        return messageRepository.save(notifyMessage);
    }

    /**
     * Updates the user's session attributes for unread messages
     * If the user has unread message(s), the last message will also be added (for the main page)
     * @param user
     * @param req
     */
    @Override
    public void updateAttributes(User user, HttpServletRequest req) {
        List<Message> unreadMessages = findByReceiverAndOpenedIs(user, 0);
        if (unreadMessages.size() > 0) {
            Message lastMessage = unreadMessages.get(unreadMessages.size()-1);
            req.getSession().setAttribute("lastMessage", lastMessage);
        }
        req.getSession().setAttribute("unreadMessages", unreadMessages.size());
    }
}
