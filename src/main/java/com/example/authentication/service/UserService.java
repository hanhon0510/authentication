package com.example.authentication.service;


import com.example.authentication.exception.UserException;
import com.example.authentication.model.User;

public interface UserService {

    public User createUser(String username, String password);

    User findUser(String username);
}
