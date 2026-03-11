package com.innowise.authenticationservice.Initializer;

import com.innowise.authenticationservice.model.entity.User;
import com.innowise.authenticationservice.repository.UserRepository;
import com.innowise.authenticationservice.util.SaltUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SaltUtil saltUtil;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, SaltUtil saltUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.saltUtil = saltUtil;
    }


    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByLogin("Admin").isEmpty()){
            User admin = new User();
            admin.setRole(User.Role.ROLE_ADMIN);
            admin.setLogin("Admin");
            admin.setPassword("admin1234567");
            admin.setPassword(saltUtil.addSalt(passwordEncoder.encode(admin.getPassword())));
            userRepository.save(admin);
        }

    }
}
