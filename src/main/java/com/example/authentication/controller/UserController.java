package com.example.authentication.controller;

import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.exception.UserException;
import com.example.authentication.model.User;
import com.example.authentication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserService userService;
    private final String key = "hash";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sign up successfully"),
            @ApiResponse(responseCode = "400", description = "All fields need to be filled"),
            @ApiResponse(responseCode = "401", description = "Your password needs to have 8 or more characters"),
            @ApiResponse(responseCode = "409", description = "Your username has been used")
    })
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRegistrationDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (username == null || password == null) {
            return new ResponseEntity<>("All fields need to be filled", HttpStatus.BAD_REQUEST);
        }
        if (password.length() < 8) {
            return new ResponseEntity<>("Your password needs to have 8 or more characters", HttpStatus.UNAUTHORIZED);
        }
        if (userService.findUser(username) != null) {
            return new ResponseEntity<>("Your username has been used", HttpStatus.CONFLICT);
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = userService.createUser(username, hashedPassword);
        return new ResponseEntity<>("Sign up successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Sign in if existed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sign in successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Wrong password"),
            @ApiResponse(responseCode = "400", description = "All fields need to be filled")
    })
    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody UserSignInDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (username == null || password == null) {
            return new ResponseEntity<>("All fields need to be filled", HttpStatus.BAD_REQUEST);
        }

        User user = userService.findUser(username);
        if (user == null) {
            return new ResponseEntity<>("Not found username", HttpStatus.NOT_FOUND);
        }

        boolean isPasswordMatch = BCrypt.checkpw(password, user.getPassword());

        if (!isPasswordMatch) {
            return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user.getPassword(), HttpStatus.OK);

    }

}
