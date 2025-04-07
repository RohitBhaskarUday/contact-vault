package com.contactvault.services.impl;

import com.contactvault.entities.User;
import com.contactvault.helpers.Constants;
import com.contactvault.helpers.ResourceNotFoundException;
import com.contactvault.repositories.UserRepository;
import com.contactvault.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        //user ID needed to be generated.
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        //encoding the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //set the user role
        user.setRoleList(Constants.ROLE_USER);
        logger.info(user.getProvider().toString());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        //Update the user data from the user.
        User userDetail = userRepository.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("user not found"));
        userDetail.setName(user.getName());
        userDetail.setEmail(user.getEmail());
        userDetail.setPassword(user.getPassword());
        userDetail.setAbout(user.getAbout());
        userDetail.setPhoneNumber(user.getPhoneNumber());
        userDetail.setProfilePicture(user.getProfilePicture());
        userDetail.setEnabled(user.isEnabled());
        userDetail.setEmailVerified(user.isEmailVerified());
        userDetail.setPhoneVerified(user.isPhoneVerified());
        userDetail.setProvider(user.getProvider());
        userDetail.setProviderUserId(user.getProviderUserId());

        //save the user in the database.
        User save = userRepository.save(userDetail);
        return Optional.of(save);
    }

    @Override
    public void deleteUser(String id) {

        User userDetails = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        userRepository.delete(userDetails);
    }

    @Override
    public boolean isUserExist(String userId) {
        User userDetails = userRepository.findById(userId).orElse(null);
        return userDetails != null;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User userDetails = userRepository.findByEmail(email).orElse(null);
        return userDetails!=null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
