package com.contactvault.controllers;


import com.contactvault.entities.User;
import com.contactvault.helpers.Helper;
import com.contactvault.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


//for every request that you take :)
@ControllerAdvice
public class RootController {

    private final Logger logger = LoggerFactory.getLogger(RootController.class);
    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){
        if(authentication==null){
            return;
        }
        System.out.println("Adding Logged in user information to the model ");
        String userName = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("user logged in is: "+userName);
        //fetch the user from the database.
        if(userName == null){
            model.addAttribute("loggedInUser", null);
        }else{
            User user = userService.getUserByEmail(userName);
            System.out.println("User Name: "+user.getName() + " " + " User Email: "+user.getEmail());
            model.addAttribute("loggedInUser", user);
        }





    }


}
