package com.example.authentication.service;

import com.example.authentication.exception.UserException;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User createUser(String username, String password) {

        User user = new User(username, password);

        return userRepository.save(user);

    }

    @Override
    public User findUser(String username) {
        System.out.println(username);
        return userRepository.findByUsername(username);
    }
}
