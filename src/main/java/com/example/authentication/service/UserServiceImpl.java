package com.example.authentication.service;

import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.config.Jwt.JwtService;
import com.example.authentication.exception.AppException;
import com.example.authentication.model.Token;
import com.example.authentication.repository.ErrorLogRepository;
import com.example.authentication.repository.TokenRepository;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.model.User;
import com.example.authentication.response.AuthenticationResponse;
import com.example.authentication.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MessageSource messageSource;
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private ErrorLogRepository errorLogRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, ErrorLogRepository errorLogRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.errorLogRepository = errorLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
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
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(username, hashedPassword);
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

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        AuthenticationResponse loginResponse = new AuthenticationResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setUsername(user.getUsername());

        return loginResponse;
    }
    public List<User> allUsers() {

        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(403, "Forbidden");
        }

        String token = authHeader.substring(7);

        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new AppException(400, "Password or username invalid"));

        if (jwtService.isValidRefreshToken(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new AuthenticationResponse(username, accessToken, refreshToken);
        }
        throw new AppException(403, "Forbidden ");

    }

}
