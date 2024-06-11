package com.example.UserAuth.repository;

import com.example.UserAuth.model.User;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void UserRepository_Save_SavesUser() {
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setPassword("password");

        User savedUser = userRepository.save(user1);

        assertThat(savedUser).isNotNull();
    }

    @Test
    public void UserRepository_FindByUsername_ThenReturnUser() {
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setPassword("password");
        userRepository.save(user1);

        User found = userRepository.findByUsername("testuser");

        assertThat(found.getUsername()).isEqualTo("testuser");
    }
}
