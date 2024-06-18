package com.example.UserAuth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.UserAuth.model.User;
import com.example.UserAuth.model.Role;
import com.example.UserAuth.exception.RoleNotFoundException;
import com.example.UserAuth.exception.UsernameAlreadyExistsException;
import com.example.UserAuth.model.UserRole;
import com.example.UserAuth.repository.RoleRepository;
import com.example.UserAuth.repository.UserRepository;
import com.example.UserAuth.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Value("${role.user}")
    private String userRoleName;

    private User user;
    private Role roleUser;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        roleUser = new Role();
        roleUser.setName(userRoleName);
    }

    @Test
    public void UserService_Save_UserAlreadyExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        Exception exception = assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.save(user);
        });
        String expectedMessage = "Username already taken";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void UserService_Save_RoleNotExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(roleRepository.findByName(roleUser.getName())).thenReturn(null);

        Exception exception = assertThrows(RoleNotFoundException.class, () -> {
            userService.save(user);
        });
        String expectedMessage = "Role not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void UserService_Save_SuccessfulSave() {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleUser);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(roleRepository.findByName(roleUser.getName())).thenReturn(roleUser);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
//        when(userRoleRepository.save(userRole)).thenReturn(userRole); // userRole here and in UserService are different objects
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(userRole);

        userService.save(user);

        verify(userRepository).save(user);
//        verify(userRoleRepository).save(userRole); // See above
        verify(userRoleRepository).save(any(UserRole.class));
        verify(bCryptPasswordEncoder).encode("password");
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void UserService_FindByUsername_SuccessIfFound() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User found_user = userService.findByUsername("testuser");

        assertEquals("testuser", found_user.getUsername());
    }

    @Test
    public void UserService_FindByUsername_NullIfNotFound() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        User found_user = userService.findByUsername("nonexistentuser");

        assertEquals(null, found_user);
    }

    @Test
    public void UserService_LoadUserByUsername_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
    }

    @Test
    public void UserService_LoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistentuser");
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}