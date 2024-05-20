package com.example.authentication.controller;

import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.config.JwtProvider;
import com.example.authentication.exception.UserResponse;
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
import org.springframework.http.HttpStatusCode;
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
        userDto.setConfirmedPassword("hieuhieu");

        when(userService.findUser(eq("hieu"))).thenReturn(null);
        when(userService.createUser(eq("hieu"), any(String.class))).thenAnswer(i -> {
            String username = i.getArgument(0);
            String password = i.getArgument(1);
            return new User(username, password);
        });

        ResponseEntity<?> response = userController.signUp(userDto);

        UserResponse userResponse =(UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(), "Expected HTTP status code to be 200");
        assert userResponse != null;
        assertEquals("Sign up successfully", userResponse.getMessage(), "The response body message is incorrect");

    }

    @Test
    public void testSignUpUsernameAlreadyExists() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("hieu");
        userDto.setPassword("hieuhieu");
        userDto.setConfirmedPassword("hieuhieu");

        User existingUser = new User("hieu", "hashedpassword");
        when(userService.findUser(eq("hieu"))).thenReturn(existingUser);

        User mockUser = userService.findUser(userDto.getUsername());
        System.out.println("Mock User: " + mockUser);

        ResponseEntity<?> response = userController.signUp(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Password or username invalid", userResponse.getMessage(), "The response body message is incorrect");

    }

    @Test
    public void testSignUpWithNotLongEnoughPassword() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("hieu");
        userDto.setPassword("short");
        userDto.setConfirmedPassword("short");

        ResponseEntity<?> response = userController.signUp(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Your password needs to have 8 or more characters", userResponse.getMessage(), "The response body message is incorrect");

    }
    @Test
    public void testSignUpWithNullUsername() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername(null);
        userDto.setPassword("hieuhieu");
        userDto.setConfirmedPassword("hieuhieu");

        ResponseEntity<?> response = userController.signUp(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Username field need to be filled", userResponse.getMessage(), "The response body message is incorrect");

    }
    @Test
    public void testSignUpWithNullPassword() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("hieu");
        userDto.setPassword(null);
        userDto.setConfirmedPassword("test");

        ResponseEntity<?> response = userController.signUp(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Password field need to be filled", userResponse.getMessage(), "The response body message is incorrect");

    }
    @Test
    public void testSignUpWithNullConfirmedPassword() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("hieu");
        userDto.setPassword("hieuhieu");
        userDto.setConfirmedPassword(null);

        ResponseEntity<?> response = userController.signUp(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Confirm password field need to be filled", userResponse.getMessage(), "The response body message is incorrect");

    }
    @Test
    public void testSignUpPasswordNotMatchedConfirmedPassword() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("hieu");
        userDto.setPassword("hieuhieu");
        userDto.setConfirmedPassword("12345678");

        ResponseEntity<?> response = userController.signUp(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Your password and confirm password are not the same", userResponse.getMessage(), "The response body message is incorrect");

    }

    @Test
    public void testLoginSuccess() {
        UserSignInDto userDto = new UserSignInDto();
        userDto.setUsername("hieu");
        userDto.setPassword("hieuhieu");

        String hashedPassword = BCrypt.hashpw("hieuhieu", BCrypt.gensalt());
        User user = new User("hieu", hashedPassword);
        when(userService.findUser(eq("hieu"))).thenReturn(user);

        String token = JwtProvider.generateToken(user);
        ResponseEntity<?> response = userController.signIn(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status code to be OK");
        assert userResponse != null;
        assertEquals(token, userResponse.getMessage(), "The token is incorrect");
    }

    @Test
    public void testLoginWithNullUsername() {
        UserSignInDto userDto = new UserSignInDto();
        userDto.setUsername(null);
        userDto.setPassword("hieuhieu");

        ResponseEntity<?> response = userController.signIn(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Username field need to be filled", userResponse.getMessage(), "The response body message is incorrect");
    }

    @Test
    public void testLoginWithNullPassword() {
        UserSignInDto userDto = new UserSignInDto();
        userDto.setUsername("hieu");
        userDto.setPassword(null);

        ResponseEntity<?> response = userController.signIn(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Password field need to be filled", userResponse.getMessage(), "The response body message is incorrect");
    }

    @Test
    public void testLoginUserNotFound() {
        UserSignInDto userDto = new UserSignInDto();
        userDto.setUsername("hieu");
        userDto.setPassword("password");

        when(userService.findUser(eq("hieu"))).thenReturn(null);

        ResponseEntity<?> response = userController.signIn(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Password or username invalid", userResponse.getMessage(), "The response body message is incorrect");
    }

    @Test
    public void testLoginWrongPassword() {
        UserSignInDto userDto = new UserSignInDto();
        userDto.setUsername("hieu");
        userDto.setPassword("wrongpassword");

        String hashedPassword = BCrypt.hashpw("correctpassword", BCrypt.gensalt());
        User user = new User("hieu", hashedPassword);
        when(userService.findUser(eq("hieu"))).thenReturn(user);

        ResponseEntity<?> response = userController.signIn(userDto);

        UserResponse userResponse = (UserResponse) response.getBody();
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode(), "Expected HTTP status code to be 400");
        assert userResponse != null;
        assertEquals("Password or username invalid", userResponse.getMessage(), "The response body message is incorrect");
    }
}
