package com.example.UserAuth.repository;

import com.example.UserAuth.model.Role;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    private String ROLE_USER = "ROLE_USER";
    private Role roleUser;

    @BeforeEach
    public void setUp() {
        roleRepository.deleteAll();
        assertThat(roleRepository.findAll()).isEmpty();

        roleUser = new Role();
        roleUser.setName(ROLE_USER);
    }

    @Test
    public void RoleRepository_Save_SavesRole() {
        Role savedRole = roleRepository.save(roleUser);

        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getName()).isEqualTo(ROLE_USER);
    }

    @Test
    public void RoleRepository_FindByName_ThenReturnRole() {
        roleRepository.save(roleUser);

        Role found = roleRepository.findByName(ROLE_USER);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo(ROLE_USER);
    }
}
