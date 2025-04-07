package com.contactvault.forms;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "Name required")
    @Size(min = 3, message = "must have 3 characters minimum")
    private String name;

    @NotBlank(message = "Email Required")
    @Email(message = "Invalid Email Address")
    private String email;

    @NotBlank(message = "Password Required")
    @Size(min = 6, message = "should contain special characters, numbers")
    private String password;

    @NotBlank(message = "Required")
    private String about;

    @Size(min=8, max = 12, message = "Invalid Number")
    private String phoneNumber;






}
