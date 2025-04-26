package com.contactvault.services;

import com.contactvault.entities.Contact;
import com.contactvault.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAllContacts();

    Contact getById(String id);

    void delete(String id);

    Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user);
    Page<Contact> searchByEmail(String emailKeyword,  int size, int page, String sortBy, String order, User user);
    Page<Contact> searchByPhoneNumber(String phoneNumber, int size, int page, String sortBy, String order, User user);

    List<Contact> getByUserId(String userId);

    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);


}
