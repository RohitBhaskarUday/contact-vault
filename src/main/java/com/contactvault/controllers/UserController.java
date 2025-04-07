package com.contactvault.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    //user dashboard page
    @GetMapping(value =  "/dashboard")
    public String userDashBoard(){
        return "user/dashboard";
    }

    //user profile page
    @GetMapping(value = "/profile")
    public String userProfile(){
        System.out.println("User Profile");
        return "user/profile";
    }

    //user add contacts page

    //user view contacts

    //user edit contacts

    //user delete contact

}
