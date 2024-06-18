package com.example.UserAuth.repository;

import com.example.UserAuth.model.Role;
import com.example.UserAuth.model.User;
import com.example.UserAuth.model.UserRole;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRoleRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @BeforeEach
    public void setUp() {
        userRoleRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void UserRoleRepository_Save_SavesUserRole() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        userRepository.save(user);

        Role role = new Role();
        role.setName("ROLE_TEST");
        roleRepository.save(role);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        UserRole savedUserRole = userRoleRepository.save(userRole);

        assertThat(savedUserRole).isNotNull();
        assertThat(savedUserRole.getUser().getUsername()).isEqualTo("testuser");
        assertThat(savedUserRole.getRole().getName()).isEqualTo("ROLE_TEST");
    }

    @Test
    public void UserRoleRepository_FindByUserIdAndRoleId_ThenReturnUserRole() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        userRepository.save(user);

        Role role = new Role();
        role.setName("ROLE_TEST");
        roleRepository.save(role);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        UserRole found = userRoleRepository.findById(userRole.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getUser().getUsername()).isEqualTo("testuser");
        assertThat(found.getRole().getName()).isEqualTo("ROLE_TEST");
    }
}
