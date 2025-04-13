package com.contactvault.config;


import com.contactvault.entities.Providers;
import com.contactvault.entities.User;
import com.contactvault.helpers.Constants;
import com.contactvault.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
    private final UserRepository userRepository;

    @Autowired
    public OAuthAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");
        //identify the provider.

        var OAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

        String authorizedClientRegistrationId = OAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        logger.info(authorizedClientRegistrationId); // to know which ID you are using i.e. which client is being used.

        DefaultOAuth2User OAuthUser = (DefaultOAuth2User)authentication.getPrincipal();

        OAuthUser.getAttributes().forEach((key, value)->{
            logger.info(key + " : " + value);
        });

        User userDetails = new User();
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setPassword("dummy");
        userDetails.setProviderUserId("");
        userDetails.setRoleList(Constants.ROLE_USER);
        userDetails.setProviderUserId(userDetails.getName());
        userDetails.setEmailVerified(true);
        userDetails.setEnabled(true);


        //google
        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
            userDetails.setEmail(OAuthUser.getAttribute("email").toString());
            userDetails.setProvider(Providers.GOOGLE);
            userDetails.setAbout("This about is created using Google");
            userDetails.setProfilePicture(OAuthUser.getAttribute("picture").toString());
            userDetails.setName(OAuthUser.getAttribute("name").toString());
            userDetails.setProviderUserId(OAuthUser.getName());
        }
        //GitHub
        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            String email =
                    OAuthUser.getAttribute("email")
                            !=null
                            ? OAuthUser.getAttribute("email").toString()
                            : OAuthUser.getAttribute("login").toString() + "@gmail.com";

            String picture =
                    OAuthUser.getAttribute("avatar_url").toString();
            String name = OAuthUser.getAttribute("login").toString();
            String providerId = OAuthUser.getName();

            userDetails.setEmail(email);
            userDetails.setProvider(Providers.GITHUB);
            userDetails.setProfilePicture(picture);
            userDetails.setName(name);
            userDetails.setAbout("This user is created using GitHub");
            userDetails.setProviderUserId(providerId);

        }
        //Linkedin
        else if(authorizedClientRegistrationId.equalsIgnoreCase("facebook")){

        }
        else{
            logger.info("OAuthAuthenticationSuccessHandler: Unknown Provider");

            userDetails.setRoleList(Constants.ROLE_USER);
        }

        User flag = userRepository.findByEmail(userDetails.getEmail().toString()).orElse(null);

        if(flag==null){
            userRepository.save(userDetails);
            logger.info("user saved: "+userDetails.getEmail().toString());

        }

        /*

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
//
//        logger.info(user.getName());
//        user.getAttributes().forEach((key, value)->{
//            logger.info("{}=>{}", key, value);
//        });
//
//        logger.info(user.getAuthorities().toString());

        //save to database
        String email = user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String picture = user.getAttribute("picture").toString();

        //create user and save to the database.
        User userDetails = new User();
        userDetails.setEmail(email);
        userDetails.setName(name);
        userDetails.setProfilePicture(picture);
        userDetails.setPassword("password");
        userDetails.setProvider(Providers.GOOGLE);
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEnabled(true);
        userDetails.setEmailVerified(true);
        userDetails.setProviderUserId(user.getName());
        userDetails.setRoleList(Constants.ROLE_USER);
        userDetails.setAbout("This account is created using Google");

        User flag = userRepository.findByEmail(email).orElse(null);

        if(flag==null){
            userRepository.save(userDetails);
            logger.info("user saved: "+email);
        }
         */




        //data to the database needs to be saved
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }


}
