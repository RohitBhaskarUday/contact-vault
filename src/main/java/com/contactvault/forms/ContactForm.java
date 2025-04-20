package com.contactvault.forms;


import com.contactvault.validators.ValidFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Name Required")
    private String name;

    @NotBlank(message = "Email Required")
    @Email(message = "Invalid Email Address")
    private String email;

    @Size(min=8, max = 12, message = "Invalid Number")
    private String phoneNumber;

    @NotBlank(message = "Address Required")
    private String address;

    private String description;

    private boolean favorite;

    private String websiteLink;

    private String linkedInLink;

    //should create a custom annotation which will validate our file
    //size and resolution
    @ValidFile(message = "Invalid File")
    private MultipartFile contactImage;

}
