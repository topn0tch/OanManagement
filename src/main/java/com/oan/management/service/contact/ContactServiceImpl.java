package com.oan.management.service.contact;

import com.oan.management.model.Contact;
import com.oan.management.model.User;
import com.oan.management.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link ContactService}
 * @author Oan Stultjens
 * @since 25/01/2018.
 */

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    ContactRepository contactRepository;

    @Override
    public List<Contact> findByUser(User user) {
        return contactRepository.findByUser(user);
    }

    @Override
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public void deleteById(Long id) {
        contactRepository.delete(contactRepository.findById(id));
    }

    @Override
    public Contact getOne(Long id) {
        return contactRepository.getOne(id);
    }
}
