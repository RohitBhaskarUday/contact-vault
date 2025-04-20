package com.contactvault.controllers;


import com.contactvault.entities.Contact;
import com.contactvault.entities.User;
import com.contactvault.forms.ContactForm;
import com.contactvault.helpers.Helper;
import com.contactvault.helpers.Message;
import com.contactvault.helpers.MessageType;
import com.contactvault.services.ContactService;
import com.contactvault.services.ImageService;
import com.contactvault.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user/contact")
public class ContactController {


    private final ContactService contactService;
    private final UserService userService;
    private final ImageService imageService;
    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    ContactController(ContactService contactService, UserService userService, ImageService imageService){
        this.contactService = contactService;
        this.userService = userService;
        this.imageService = imageService;
    }

    //add contact page: handler
    @RequestMapping("/add")
    public String addContactView(Model model){

        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);

        return "user/add_contact";
    }

    //add contacts and save
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult,
                              Authentication authentication, HttpSession httpSession){
        System.out.println("COntact form "+contactForm);
        String userName = Helper.getEmailOfLoggedInUser(authentication);

        //validate the form data
        if(bindingResult.hasErrors()){

            bindingResult.getAllErrors().forEach(error -> logger.info(error.toString()));

            Message msg = Message.builder()
                    .content("Please provide valid inputs.")
                    .type(MessageType.red)
                    .build();
            httpSession.setAttribute("message",msg);
            return "user/add_contact";
        }

        //Extract the form to the Contact Object;
        logger.info("file information : {}", contactForm.getContactImage().getOriginalFilename());

        Contact contactDetails = new Contact();

        contactDetails.setName(contactForm.getName());
        contactDetails.setEmail(contactForm.getEmail());
        contactDetails.setPhoneNumber(contactForm.getPhoneNumber());
        contactDetails.setAddress(contactForm.getAddress());
        contactDetails.setDescription(contactForm.getDescription());
        contactDetails.setWebsiteLink(contactForm.getWebsiteLink());
        contactDetails.setLinkedInLink(contactForm.getLinkedInLink());
        contactDetails.setUser(userService.getUserByEmail(userName));
        contactDetails.setFavorite(contactForm.isFavorite());

        //process the image
        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
            contactDetails.setPicture(fileURL);
            contactDetails.setCloudinaryImagePublicId(filename);

        }

        Message msg = Message.builder()
                .content("You have successfully added a new contact!").type(MessageType.green)
                .build();
        httpSession.setAttribute("message", msg);


        //process the data to DB
        contactService.save(contactDetails);
        return "redirect:/user/contact/add";
    }

    //view contacts
    @RequestMapping
    public String viewContacts(Model model, Authentication authentication){
        //first know who the logged-in User is.
        String loggedInUser = Helper.getEmailOfLoggedInUser(authentication);
        User userName = userService.getUserByEmail(loggedInUser);

        //load all the currently logged-in user contacts
        List<Contact> contactList = contactService.getByUser(userName);
        model.addAttribute("contacts", contactList);

        return "user/contact";
    }
}
