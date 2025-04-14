package com.contactvault.controllers;


import com.contactvault.entities.User;
import com.contactvault.helpers.Helper;
import com.contactvault.services.UserService;
import jakarta.validation.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }


    //user dashboard page
    @GetMapping(value =  "/dashboard")
    public String userDashBoard(){
        return "user/dashboard";
    }

    //user profile page
    @GetMapping(value = "/profile")
    public String userProfile(Model model, Authentication authentication){

        String userName = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("user logged in is: "+userName);
        //fetch the user from the database.
        User user = userService.getUserByEmail(userName);
        System.out.println("User Name: "+user.getName() + " " + " User Email: "+user.getEmail());

        model.addAttribute("loggedInUser", user);



        System.out.println();
        return "user/profile";
    }

    //user add contacts page

    //user view contacts

    //user edit contacts

    //user delete contact

}
