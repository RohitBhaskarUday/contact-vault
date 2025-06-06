package com.contactvault.controllers;


import com.contactvault.entities.Contact;
import com.contactvault.entities.User;
import com.contactvault.forms.ContactForm;
import com.contactvault.forms.ContactSearchForm;
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
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String viewContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                               @RequestParam(value = "direction", defaultValue = "asc") String direction,
                               Model model, Authentication authentication
                               ){
        //first know who the logged-in User is.
        String loggedInUser = Helper.getEmailOfLoggedInUser(authentication);
        User userName = userService.getUserByEmail(loggedInUser);
        //load all the currently logged-in user contacts
        Page<Contact> contactPage = contactService.getByUser(userName, page, size, sortBy, direction);
        model.addAttribute("contactPage", contactPage);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contact";
    }

    //search Handler
    @GetMapping("/search")
    public String searchHandler(@ModelAttribute ContactSearchForm contactSearchForm,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                                @RequestParam(value = "direction", defaultValue = "asc") String direction,
                                Model model, Authentication authentication){
        logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue() );

        User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));



        Page<Contact> contactPage = null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            contactPage = contactService.searchByName(contactSearchForm.getValue(),size, page, sortBy, direction, user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            contactPage = contactService.searchByEmail(contactSearchForm.getValue(),size, page, sortBy, direction, user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            contactPage = contactService.searchByPhoneNumber(contactSearchForm.getValue(),size, page, sortBy, direction, user);
        }

        logger.info("contactPage {} ", contactPage);
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("contactPage", contactPage);
        return "user/search";
    }

    //delete contact
    @RequestMapping("/delete/{contactId}")
    public String deleteHandler(@PathVariable String contactId,
                                HttpSession session){
        contactService.delete(contactId);
        logger.info("contactId {} deleted", contactId);

        session.setAttribute("message",
                Message.builder()
                        .content("Contact deleted successfully!")
                        .type(MessageType.green)
                        .build());

        return "redirect:/user/contact";
    }

    //update contact
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable String contactId,
                                        Model model){

        var contact = contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update_contact_view";
    }

    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable String contactId,
                                @Valid @ModelAttribute ContactForm contactForm,
                                BindingResult bindingResult,
                                Model model){

        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }


        Contact updateContactDetails = contactService.getById(contactId);
        updateContactDetails.setId(contactId);
        updateContactDetails.setName(contactForm.getName());
        updateContactDetails.setEmail(contactForm.getEmail());
        updateContactDetails.setPhoneNumber(contactForm.getPhoneNumber());
        updateContactDetails.setAddress(contactForm.getAddress());
        updateContactDetails.setDescription(contactForm.getDescription());
        updateContactDetails.setFavorite(contactForm.isFavorite());
        updateContactDetails.setWebsiteLink(contactForm.getWebsiteLink());
        updateContactDetails.setLinkedInLink(contactForm.getLinkedInLink());

       if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
           //process image
           logger.info("file is not empty");
           String fileName = UUID.randomUUID().toString();
           String imageURL= imageService.uploadImage(contactForm.getContactImage(), contactId);
           updateContactDetails.setCloudinaryImagePublicId(fileName);
           updateContactDetails.setPicture(imageURL);
           //for changing the image in Front-End
           contactForm.setPicture(imageURL);


       }

        var updatedDetails = contactService.update(updateContactDetails);
        logger.info("updated contact {}", updatedDetails);

        //message after success
        model.addAttribute("message", Message.builder()
                .content("Your Contact has been updated")
                .type(MessageType.green).build());



        return "redirect:/user/contact/view/"+contactId;
    }

}
