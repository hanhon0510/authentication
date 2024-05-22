package com.example.authentication.service;

import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.config.JwtProvider;
import com.example.authentication.exception.AppException;
import com.example.authentication.repository.ErrorLogRepository;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.model.User;
import com.example.authentication.response.AuthenticationResponse;
import com.example.authentication.response.UserResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MessageSource messageSource;
    private UserRepository userRepository;

    private ErrorLogRepository errorLogRepository;

    public UserServiceImpl(UserRepository userRepository, ErrorLogRepository errorLogRepository) {
        this.userRepository = userRepository;
        this.errorLogRepository = errorLogRepository;
    }


    @Override
    public UserResponse createUser(UserRegistrationDto userRegistrationDto, Locale locale) {
        String username = userRegistrationDto.getUsername();
        String password = userRegistrationDto.getPassword();
        String confirmedPassword = userRegistrationDto.getConfirmedPassword();

        if (password.length() < 8) {
            throw new AppException(400, messageSource.getMessage("shortPassword", null, locale));
        }

        if (!password.equals(confirmedPassword)) {
            throw new AppException(400, messageSource.getMessage("passwordAndConfirmPasswordNotMatch", null, locale));
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new AppException(400, messageSource.getMessage("invalidInput", null, locale));
        }

        User user = new User(username, password);
        userRepository.save(user);
        return UserResponse.builder()
                .username(username)
                .build();

    }

    @Override
    public AuthenticationResponse login(UserSignInDto userSignInDto, Locale locale) {
        String username = userSignInDto.getUsername();
        String password = userSignInDto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new AppException(400, messageSource.getMessage("invalidInput", null, locale)));

        boolean isPasswordMatch = BCrypt.checkpw(password, user.getPassword());

        if (!isPasswordMatch) {
            throw new AppException(400, messageSource.getMessage("invalidInput", null, locale));
        }

        String token = JwtProvider.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .type("jwt")
                .build();
    }
}
