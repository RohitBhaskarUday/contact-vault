package com.contactvault.entities;


import com.contactvault.helpers.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Builder;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String userId;
    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(length = 1000)
    private String about;
    private String profilePicture;
    private String phoneNumber;

    //info

    private boolean enabled = true;
    private boolean emailVerified = true;
    private  boolean phoneVerified = false;

    //self signup or // with Google or // with facebook or // with Twitter or // with git

    @Enumerated(value = EnumType.STRING)
    private Providers provider = Providers.SELF;

    private String providerUserId;


    //more fields mapping
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contact> contacts = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //list of roles[User, Admin]
        //collection of SimpleGrantedAuthority[roles{Admin, User}]

       Collection<SimpleGrantedAuthority> collections = roleList.stream()
               .map(role-> new SimpleGrantedAuthority(role))
               .collect(Collectors.toList());
       return collections;
    }

    //user-name is the email-id

    @Override
    public String getUsername() {
        return this.email;

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }

    @Override
    public String getPassword(){
        return this.password;
    }


    public void setRoleList(Constants role){
        this.roleList.add(role.name());
    }

}
