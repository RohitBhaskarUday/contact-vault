package com.contactvault.controllers;


import com.contactvault.entities.Contact;
import com.contactvault.services.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    private final ContactService contactService;
    Logger logger = LoggerFactory.getLogger(APIController.class);

    @Autowired
    APIController(ContactService contactService){
        this.contactService = contactService;
    }

    //get contact of the user
    @GetMapping("/contact/{contactId}")
    public Contact getContact(@PathVariable String contactId){
        return contactService.getById(contactId);
    }





}
