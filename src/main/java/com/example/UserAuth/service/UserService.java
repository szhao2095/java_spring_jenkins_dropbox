package com.example.UserAuth.service;

import com.example.UserAuth.exception.UsernameAlreadyExistsException;
import com.example.UserAuth.model.User;
import com.example.UserAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user) throws UsernameAlreadyExistsException {
        logger.info("Attempting to save user: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()) != null) {
            logger.warn("Username already taken: {}", user.getUsername());
            throw new UsernameAlreadyExistsException("Username already taken");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("User saved successfully: {}", user.getUsername());
    }

    public User findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            logger.info("User found: {}", username);
        } else {
            logger.warn("User not found: {}", username);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.error("User not found: {}", username);
            throw new UsernameNotFoundException("User not found");
        }
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        builder.password(user.getPassword());
        builder.roles("USER");
        logger.info("User loaded successfully: {}", username);
        return builder.build();
    }
}
