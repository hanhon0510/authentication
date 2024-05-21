package com.example.authentication.controller;

import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.config.JwtProvider;
import com.example.authentication.exception.AppException;
import com.example.authentication.response.AuthenticationResponse;
import com.example.authentication.response.BaseResponse;
import com.example.authentication.response.UserResponse;
import com.example.authentication.model.User;
import com.example.authentication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RestController
public class UserController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create new user",
            description = "Create a new account that does not exist yet.")
    @PostMapping("/signup")
    public BaseResponse<UserResponse> createNewUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto,
                                                    @RequestParam(name = "lang", required = true) String lang) {
        Locale locale = Locale.US;
        if (lang == null) {
            throw new AppException(400, messageSource.getMessage("nullLang", null, locale));
        }
        locale = new Locale(lang);
        if (userRegistrationDto.getUsername() == null) {
            throw new AppException(400, messageSource.getMessage("nullUsername", null, locale));
        }

        if (userRegistrationDto.getPassword() == null) {
            throw new AppException(400, messageSource.getMessage("nullPassword", null, locale));
        }

        if (userRegistrationDto.getConfirmedPassword() == null) {
            throw new AppException(400, messageSource.getMessage("nullConfirmPassword", null, locale));
        }

        return BaseResponse.<UserResponse>builder()
                .message("Sign up successfully")
                .data(userService.createUser(userRegistrationDto, locale))
                .build();
    }
    @Operation(summary = "Log in if existed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successfully"),
            @ApiResponse(responseCode = "400", description = "Password or username invalid"),
            @ApiResponse(responseCode = "400", description = "Username field need to be filled"),
            @ApiResponse(responseCode = "400", description = "Password field need to be filled"),
    })
    @PostMapping("/login")
    BaseResponse<AuthenticationResponse> login(@RequestBody @Valid UserSignInDto userSignInDto,
                                               @RequestParam(name = "lang", required = true) String lang) {

        Locale locale = Locale.US;
        if (lang == null) {
            throw new AppException(400, messageSource.getMessage("nullLang", null, locale));
        }
        locale = new Locale(lang);
        if (userSignInDto.getUsername() == null) {
            throw new AppException(400, messageSource.getMessage("nullUsername", null, locale));
        }

        if (userSignInDto.getPassword() == null) {
            throw new AppException(400, messageSource.getMessage("nullPassword", null, locale));
        }

        return BaseResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("Login successfully")
                .data(userService.login(userSignInDto, locale))
                .build();
    }


//    @Operation(summary = "Log in if existed")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Login successfully"),
//            @ApiResponse(responseCode = "400", description = "Password or username invalid"),
//            @ApiResponse(responseCode = "400", description = "Username field need to be filled"),
//            @ApiResponse(responseCode = "400", description = "Password field need to be filled"),
//    })
//    @PostMapping("/login")
//    public ResponseEntity<?> signIn(@RequestBody UserSignInDto userDto,
//                                    @RequestParam(name = "lang", required = true) String lang) {
//        String username = userDto.getUsername();
//        String password = userDto.getPassword();
//        Locale locale = Locale.US;
//        if (lang != null) {
//            locale = new Locale(lang);
//        }
//
//        try {
//            String nullUsername = messageSource.getMessage("nullUsername", null, locale);
//            String nullPassword = messageSource.getMessage("nullPassword", null, locale);
//            String invalidInput = messageSource.getMessage("invalidInput", null, locale);
//            String loginSuccess = messageSource.getMessage("loginSuccess", null, locale);
//
//            if (username == null) {
//                return ResponseEntity.status(400).body(new UserResponse(400, "Bad request", nullUsername));
//            }
//            if (password == null) {
//                return ResponseEntity.status(400).body(new UserResponse(400, "Bad request", nullPassword));
//            }
//
//            User user = userService.findUser(username);
//            if (user == null) {
//                return ResponseEntity.status(400).body(new UserResponse(400, "Bad request", invalidInput));
//            }
//
//            boolean isPasswordMatch = BCrypt.checkpw(password, user.getPassword());
//
//            if (!isPasswordMatch) {
//                return ResponseEntity.status(400).body(new UserResponse(400, "Bad request", invalidInput));
//
//            }
//            String token = JwtProvider.generateToken(user);
//            return ResponseEntity.status(200).body(new UserResponse(200, "OK", token));
//        } catch (NoSuchMessageException e) {
//            return new ResponseEntity<>("Message not found", HttpStatus.NOT_FOUND);
//        }
//    }
}
// xoa lib thua, cmt
//return response
//sua ma thanh 400
//da ngon ngu -> header them language = en
//
//message, stack trace trong db
//send mail cho admin
