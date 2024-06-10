package com.example.authentication.service;


import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.model.User;
import com.example.authentication.response.AuthenticationResponse;
import com.example.authentication.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Locale;

public interface UserService {

    public UserResponse createUser(UserRegistrationDto userRegistrationDto, Locale locale);

    public AuthenticationResponse login(UserSignInDto userSignInDto, Locale locale);

    public List<User> allUsers();

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
