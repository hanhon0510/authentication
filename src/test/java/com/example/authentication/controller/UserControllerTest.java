package com.example.authentication.controller;

import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.model.User;
import com.example.authentication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUpSuccess() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("hieu");
        userDto.setPassword("hieuhieu");

        when(userService.findUser(eq("hieu"))).thenReturn(null);
        when(userService.createUser(eq("hieu"), any(String.class))).thenAnswer(i -> {
            String username = i.getArgument(0);
            String password = i.getArgument(1);
            return new User(username, password);
        });

        ResponseEntity<String> response = userController.signUp(userDto);

        assert response.getStatusCode() == HttpStatus.CREATED;
    }

    @Test
    public void testSignUpUsernameAlreadyExists() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("hieu");
        userDto.setPassword("hieuhieu");

        User existingUser = new User("hieu", "hashedpassword");
        when(userService.findUser(eq("hieu"))).thenReturn(existingUser);

        User mockUser = userService.findUser(userDto.getUsername());
        System.out.println("Mock User: " + mockUser);

        ResponseEntity<String> response = userController.signUp(userDto);

        assert response.getStatusCode() == HttpStatus.CONFLICT;
    }

    @Test
    public void testSignUpWithNotLongEnoughPassword() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("hieu");
        userDto.setPassword("short");

        ResponseEntity<String> response = userController.signUp(userDto);

        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    public void testSignInSuccess() {
        UserSignInDto userDto = new UserSignInDto();
        userDto.setUsername("hieu");
        userDto.setPassword("hieuhieu");

        String hashedPassword = BCrypt.hashpw("hieuhieu", BCrypt.gensalt());
        User user = new User("hieu", hashedPassword);
        when(userService.findUser(eq("hieu"))).thenReturn(user);

        ResponseEntity<String> response = userController.signIn(userDto);

        assert response.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testSignInUserNotFound() {
        UserSignInDto userDto = new UserSignInDto();
        userDto.setUsername("hieu");
        userDto.setPassword("password");

        when(userService.findUser(eq("hieu"))).thenReturn(null);

        ResponseEntity<String> response = userController.signIn(userDto);
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void testSignInWrongPassword() {
        UserSignInDto userDto = new UserSignInDto();
        userDto.setUsername("hieu");
        userDto.setPassword("wrongpassword");

        String hashedPassword = BCrypt.hashpw("correctpassword", BCrypt.gensalt());
        User user = new User("hieu", hashedPassword);
        when(userService.findUser(eq("hieu"))).thenReturn(user);

        ResponseEntity<String> response = userController.signIn(userDto);
        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }
}
