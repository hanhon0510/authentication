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
import com.example.authentication.utils.MailUtil;
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
}

