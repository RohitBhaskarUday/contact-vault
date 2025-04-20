package com.contactvault.services;

import com.contactvault.entities.Contact;
import com.contactvault.entities.User;

import java.util.List;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAllContacts();

    Contact getContactById(String id);

    void delete(String id);

    List<Contact> search(String name, String email, String phoneNumber);

    List<Contact> getByUserId(String userId);

    List<Contact> getByUser(User user);

}
