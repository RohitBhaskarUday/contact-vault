package com.contactvault.repositories;

import com.contactvault.entities.Contact;
import com.contactvault.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, String> {

    //find the contact by user.
    //custom finder method
    List<Contact> findByUser(User user);


    //custom query method
    @Query("SELECT c from Contact c where c.user.userId = : userId")
    List<Contact> findByUserId(@Param("userId") String userId);

}
