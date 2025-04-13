package com.contactvault.repositories;

import com.contactvault.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    //Methods related to DB operations

    //Custom Query methods


    //custom finder methods
    Optional <User> findByEmail(String email);
    Optional <User> findByEmailAndPassword(String email, String password);

}
