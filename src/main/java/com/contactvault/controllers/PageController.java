package com.contactvault.controllers;


import com.contactvault.entities.User;
import com.contactvault.forms.UserForm;
import com.contactvault.helpers.Message;
import com.contactvault.helpers.MessageType;
import com.contactvault.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(){
        return "redirect:/home";
    }


    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home Page Handler");
        model.addAttribute("name", "Contact-Vault");
        model.addAttribute("GitHubRepo", "https://github.com/RohitBhaskarUday/contact-vault");
        return "home";
    }

    //about
    @RequestMapping("/about")
    public String aboutPage(){
        System.out.println("about page loading");
        return "about";
    }

    //services
    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("services page loading");
        return "services";
    }


    @RequestMapping("/contact")
    public String contactPage(){
        System.out.println("contact us page loading");
        return "contact";
    }

//this is the login controller
    @GetMapping("/login")
    public String login(){
        System.out.println("contact us page loading");
        return "login";
    }

    //this is actually the registration controller  -- view
    @GetMapping(value = "/register")
    public String register(Model model){

        UserForm userForm = new UserForm();
        //can add default data also
        model.addAttribute("userForm", userForm);

        System.out.println("contact us page loading");
        return "register";
    }

    //processing the register
    @PostMapping(value = "/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){
        System.out.println("processing the registered user");
        //fetch the form data
        //User form
        System.out.println(userForm);
        //validate the form data
        if(rBindingResult.hasErrors()){
            return "register";
        }

        //save it to the db.


        //user service

        //extract from userForm to the User
        User user = new User();

        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePicture("default.png");

        User saveUser = userService.saveUser(user);
        System.out.println("user saved");

        //message = "Registration successful"
        Message msg = Message.builder().content("Registration Successful").type(MessageType.GREEN).build();
        session.setAttribute("message", msg);


        //redirect to the login page.
        return "redirect:/register";
    }
}
