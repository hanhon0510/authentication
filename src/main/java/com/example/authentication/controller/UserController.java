package com.example.authentication.controller;

import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.config.localization.MyLocalResolver;
import com.example.authentication.exception.AppException;
import com.example.authentication.response.AuthenticationResponse;
import com.example.authentication.response.BaseResponse;
import com.example.authentication.response.UserResponse;
import com.example.authentication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RestController
public class UserController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    MyLocalResolver myLocalResolver;

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Create new user",
            description = "Create a new account that does not exist yet.")
    @PostMapping("/signup")
    public BaseResponse<UserResponse> createNewUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto,
                                                    HttpServletRequest request) {
        
       Locale locale = myLocalResolver.resolveLocale(request);
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
                .message(messageSource.getMessage("signUpSuccess", null, locale))
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
                                               HttpServletRequest request) {

        Locale locale = myLocalResolver.resolveLocale(request);
        if (userSignInDto.getUsername() == null) {
            throw new AppException(400, messageSource.getMessage("nullUsername", null, locale));
        }

        if (userSignInDto.getPassword() == null) {
            throw new AppException(400, messageSource.getMessage("nullPassword", null, locale));
        }

        return BaseResponse.<AuthenticationResponse>builder()
                .code(200)
                .message(messageSource.getMessage("loginSuccess", null, locale))
                .data(userService.login(userSignInDto, locale))
                .build();
    }
}

