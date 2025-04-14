package com.contactvault.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){


        // if the user is logged-in with email and password
        if(authentication instanceof OAuth2AuthenticationToken) {

            var oAuth2Token = (OAuth2AuthenticationToken) authentication;
            var clientId = oAuth2Token.getAuthorizedClientRegistrationId();

            var oAuth2UserDetails = (OAuth2User) authentication.getPrincipal();
            String userName = "";

            //if google
            if(clientId.equalsIgnoreCase("google")){
                System.out.println("Getting email from google");

                userName = oAuth2UserDetails.getAttribute("email").toString();


            }//if GitHub
            else if (clientId.equalsIgnoreCase("github")) {
                System.out.println("Getting email from github");
                userName= oAuth2UserDetails.getAttribute("email")
                        !=null
                        ? oAuth2UserDetails.getAttribute("email").toString()
                        : oAuth2UserDetails.getAttribute("login").toString() + "@gmail.com";
            }

            return userName;

        }else{
            System.out.println("getting data from the database");
            return authentication.getName();
        }

    }

}
