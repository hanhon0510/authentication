package com.example.authentication.service;


import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.model.User;
import com.example.authentication.response.AuthenticationResponse;
import com.example.authentication.response.UserResponse;

import java.util.Locale;

public interface UserService {

    public UserResponse createUser(UserRegistrationDto userRegistrationDto, Locale locale);

//    User findUser(String username);

    AuthenticationResponse login(UserSignInDto userSignInDto, Locale locale);
}
