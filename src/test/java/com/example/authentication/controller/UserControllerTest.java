//package com.example.authentication.controller;
//
//import com.example.authentication.DTO.UserRegistrationDto;
//import com.example.authentication.DTO.UserSignInDto;
//import com.example.authentication.config.localization.MyLocalResolver;
//import com.example.authentication.exception.AppException;
//import com.example.authentication.repository.UserRepository;
//import com.example.authentication.response.AuthenticationResponse;
//import com.example.authentication.response.BaseResponse;
//import com.example.authentication.model.User;
//import com.example.authentication.service.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.mindrot.jbcrypt.BCrypt;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.context.MessageSource;
//import org.springframework.web.servlet.LocaleResolver;
//
//import java.util.Locale;
//import java.util.Optional;
//
//@SpringBootTest
//public class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//    @InjectMocks
//    private UserController userController;
//    @Mock
//    private MessageSource messageSource;
//    @Mock
//    private LocaleResolver localeResolver;
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private MyLocalResolver myLocalResolver;
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testSignUpUsernameAlreadyExists() {
//        UserRegistrationDto userDto = new UserRegistrationDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword("hieuhieu");
//        userDto.setConfirmedPassword("hieuhieu");
//
//        String lang = "en";
//        Locale locale = Locale.forLanguageTag(lang);
//
//        User existingUser = new User();
//        existingUser.setUsername("hieu");
//        existingUser.setPassword("hashedpassword");
//
//        // Mock the message source to return the expected message
//        when(messageSource.getMessage("username.exists", null, locale)).thenReturn("Password or username invalid");
//
//        // Mock the findUser method to return an existing user
//        when(userRepository.findByUsername(eq("hieu"))).thenReturn(Optional.of(existingUser));
//
//        // Execute the sign-up method
//        try {
//            userController.createNewUser(userDto, request);
//        } catch (AppException ex) {
//            // Assert the exception
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//            assertEquals("Password or username invalid", ex.getMessage(), "The response body message is incorrect");
//        }
//    }
//
//    @Test
//    public void testSignUpWithNotLongEnoughPassword() {
//        UserRegistrationDto userDto = new UserRegistrationDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword("short");
//        userDto.setConfirmedPassword("short");
//
//        String lang = "en";
//        Locale locale = Locale.forLanguageTag(lang);
//
//        // Mock the message source to return the expected message
//        when(messageSource.getMessage("password.short", null, locale)).thenReturn("Your password needs to have 8 or more characters");
//
//        // Execute the sign-up method
//        try {
//            userController.createNewUser(userDto, request);
//        } catch (AppException ex) {
//            // Assert the exception
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//            assertEquals("Your password needs to have 8 or more characters", ex.getMessage(), "The response body message is incorrect");
//        }
//
//    }
//    @Test
//    public void testSignUpWithNullUsername() {
//        UserRegistrationDto userDto = new UserRegistrationDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword(null);
//        userDto.setConfirmedPassword("test");
//
//        String lang = "en";
//        Locale locale = new Locale(lang);
//
//        when(messageSource.getMessage("nullPassword", null, locale)).thenReturn("Username field need to be filled");
//
//        try {
//            userController.createNewUser(userDto, request);
//        } catch (AppException ex) {
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//        }
//
//    }
//    @Test
//    public void testSignUpWithNullPassword() {
//        UserRegistrationDto userDto = new UserRegistrationDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword(null);
//        userDto.setConfirmedPassword("test");
//
//        String lang = "en";
//        Locale locale = new Locale(lang);
//
//        when(messageSource.getMessage("nullPassword", null, locale)).thenReturn("Password field need to be filled");
//
//        try {
//            userController.createNewUser(userDto, request);
//        } catch (AppException ex) {
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//        }
//    }
//    @Test
//    public void testSignUpWithNullConfirmedPassword() {
//        UserRegistrationDto userDto = new UserRegistrationDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword("test");
//        userDto.setConfirmedPassword(null);
//
//        String lang = "en";
//        Locale locale = new Locale(lang);
//
//        when(messageSource.getMessage("nullConfirmPassword", null, locale)).thenReturn("Confirm password field need to be filled");
//
//        try {
//            userController.createNewUser(userDto, request);
//        } catch (AppException ex) {
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//        }
//
//    }
//    @Test
//    public void testSignUpPasswordNotMatchedConfirmedPassword() {
//        UserRegistrationDto userDto = new UserRegistrationDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword("hieuhieu");
//        userDto.setConfirmedPassword("12345678");
//        String lang = "en";
//        Locale locale = Locale.forLanguageTag(lang);
//
//        when(messageSource.getMessage("passwordAndConfirmPasswordNotMatch", null, locale)).thenReturn("Your password and confirm password are not the same");
//
//        try {
//            userController.createNewUser(userDto, request);
//        } catch (AppException ex) {
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//            assertEquals("Your password and confirm password are not the same", ex.getMessage(), "The response body message is incorrect");
//        }
//
//    }
//
//    @Test
//    public void testLoginSuccess() {
//        UserSignInDto userDto = new UserSignInDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword("hieuhieu");
//
//        String lang = "en";
//        Locale locale = Locale.forLanguageTag(lang);
//
//        String token = "generated-jwt-token";
//        AuthenticationResponse authResponse = AuthenticationResponse.builder()
//                .token(token)
//                .type("jwt")
//                .build();
//
//        String hashedPassword = BCrypt.hashpw("hieuhieu", BCrypt.gensalt());
//        User user = new User("hieu", hashedPassword);
//        when(userService.login(any(UserSignInDto.class), eq(locale))).thenReturn(authResponse);
//
//        BaseResponse<AuthenticationResponse> response = userController.login(userDto, request);
//
//        assertEquals(200, response.getCode(), "Expected HTTP status code to be OK");
//    }
//
//    @Test
//    public void testLoginWithNullUsername() {
//        UserSignInDto userDto = new UserSignInDto();
//        userDto.setUsername(null);
//        userDto.setPassword("hieuhieu");
//        String lang = "en";
//        Locale locale = Locale.forLanguageTag(lang);
//
//        when(messageSource.getMessage("nullUsername", null, locale)).thenReturn("Username field needs to be filled");
//
//        try {
//            userController.login(userDto, request);
//        } catch (AppException ex) {
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//        }
//    }
//
//    @Test
//    public void testLoginWithNullPassword() {
//        UserSignInDto userDto = new UserSignInDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword(null);
//        String lang = "en";
//        Locale locale = Locale.forLanguageTag(lang);
//
//        when(messageSource.getMessage("nullPassword", null, locale)).thenReturn("Password field needs to be filled");
//
//        try {
//            userController.login(userDto, request);
//        } catch (AppException ex) {
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//        }
//    }
//
//    @Test
//    public void testLoginUserNotFound() {
//        UserSignInDto userDto = new UserSignInDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword("password");
//        String lang = "en";
//        Locale locale = Locale.forLanguageTag(lang);
//
////        when(userService.login(any(UserSignInDto.class), eq(locale))).thenThrow(new AppException(400, messageSource.getMessage("invalidInput", null, locale)));
//        when(userService.login(any(UserSignInDto.class), eq(locale))).thenReturn(AuthenticationResponse.builder().build());
//
//        try {
//            userController.login(userDto, request);
//        } catch (AppException ex) {
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//            assertEquals("Invalid username or password", ex.getMessage(), "The response body message is incorrect");
//        }  }
//
//    @Test
//    public void testLoginWrongPassword() {
//        UserSignInDto userDto = new UserSignInDto();
//        userDto.setUsername("hieu");
//        userDto.setPassword("wrongpassword");
//        String lang = "en";
//        Locale locale = Locale.forLanguageTag(lang);
//
//        when(userService.login(any(UserSignInDto.class), eq(locale))).thenReturn(AuthenticationResponse.builder().build());
//
//        try {
//            userController.login(userDto, request);
//        } catch (AppException ex) {
//            assertEquals(400, ex.getStatusCode(), "Expected HTTP status code to be 400");
//            assertEquals("Invalid username or password", ex.getMessage(), "The response body message is incorrect");
//        }
//    }
//}
