package com.contactvault.services.impl;

import com.contactvault.entities.Contact;
import com.contactvault.entities.User;
import com.contactvault.helpers.ResourceNotFoundException;
import com.contactvault.repositories.ContactRepository;
import com.contactvault.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        var updatedContactDetails =contactRepository.findById(contact.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Contact not found"));

        updatedContactDetails.setName(contact.getName());
        updatedContactDetails.setEmail(contact.getEmail());
        updatedContactDetails.setPhoneNumber(contact.getPhoneNumber());
        updatedContactDetails.setAddress(contact.getAddress());
        updatedContactDetails.setDescription(contact.getDescription());
        updatedContactDetails.setFavorite(contact.isFavorite());
        updatedContactDetails.setPicture(contact.getPicture());
        updatedContactDetails.setWebsiteLink(contact.getWebsiteLink());
        updatedContactDetails.setLinkedInLink(contact.getLinkedInLink());
        updatedContactDetails.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());


        return contactRepository.save(updatedContactDetails);

    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getById(String id) {
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
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndNameContaining( user, nameKeyword, pageable);

    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumber, int size, int page, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndPhoneNumberContaining( user, phoneNumber, pageable);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepository.findByUserId(userId);
    }


    @Override
    public Page<Contact> getByUser(User user, int page, int size,String sortBy, String direction) {
        Sort sort = direction.equals("desc")?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUser(user, pageable);
    }

}
