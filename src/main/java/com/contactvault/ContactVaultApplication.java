package com.contactvault;

import com.contactvault.entities.User;
import com.contactvault.helpers.Constants;
import com.contactvault.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ContactVaultApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ContactVaultApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setUserId(UUID.randomUUID().toString());
		user.setName("admin");
		user.setEmail("admin@gmail.com");
		user.setPassword(passwordEncoder.encode("admin"));
		user.setRoleList(Constants.ROLE_USER);
		user.setEmailVerified(true);
		user.setEnabled(true);
		user.setAbout("This is dummy user created initially");
		user.setPhoneVerified(true);

		userRepository.findByEmail("admin@gmail.com").ifPresentOrElse(user1 -> {},() -> {
			userRepository.save(user);
			System.out.println("user created");
		});


	}

}
