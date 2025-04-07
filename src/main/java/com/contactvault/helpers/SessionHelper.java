package com.contactvault.helpers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;


@Component
public class SessionHelper {

    public static void removeMessage(){

        try{
            System.out.println("Removing the msg from the session. ");
            HttpSession session =((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                    .getRequest().getSession();
            session.removeAttribute("message");

        }catch (Exception e){
            System.out.println("Error Occurred at the Session Helper "+e);
            e.printStackTrace();
        }

    }

}
