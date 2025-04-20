package com.contactvault.services.impl;

import com.contactvault.entities.Contact;
import com.contactvault.entities.User;
import com.contactvault.forms.ContactForm;
import com.contactvault.helpers.ResourceNotFoundException;
import com.contactvault.repositories.ContactRepository;
import com.contactvault.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Component
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepository.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        //TODO
        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public void delete(String id) {
        var contact =  contactRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        contactRepository.delete(contact);
    }

    @Override
    public List<Contact> search(String name, String email, String phoneNumber) {
        //TODO;
        return List.of();
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepository.findByUserId(userId);
    }

    @Override
    public List<Contact> getByUser(User user) {
        return contactRepository.findByUser(user);
    }
}
