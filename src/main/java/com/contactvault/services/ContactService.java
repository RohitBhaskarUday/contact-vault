package com.contactvault.services;

import com.contactvault.entities.Contact;
import com.contactvault.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAllContacts();

    Contact getContactById(String id);

    void delete(String id);

    List<Contact> search(String name, String email, String phoneNumber);

    List<Contact> getByUserId(String userId);


    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);

}
