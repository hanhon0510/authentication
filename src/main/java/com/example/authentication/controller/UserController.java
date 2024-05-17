package com.example.authentication.controller;

import com.example.authentication.DTO.UserRegistrationDto;
import com.example.authentication.DTO.UserSignInDto;
import com.example.authentication.config.JwtProvider;
import com.example.authentication.exception.UserResponse;
import com.example.authentication.model.User;
import com.example.authentication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sign up successfully"),
            @ApiResponse(responseCode = "400", description = "All fields need to be filled"),
            @ApiResponse(responseCode = "400", description = "Your password needs to have 8 or more characters"),
            @ApiResponse(responseCode = "400", description = "Password or username invalid")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRegistrationDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (username == null || password == null) {
            return ResponseEntity.status(400).body(new UserResponse(400, "Bad request", "All fields need to be filled"));
        }
        if (password.length() < 8) {
            return ResponseEntity.status(400).body(new UserResponse(400,"Bad request", "Your password needs to have 8 or more characters"));

        }
        if (userService.findUser(username) != null) {
            return ResponseEntity.status(400).body(new UserResponse(400, "Bad request", "Password or username invalid"));
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = userService.createUser(username, hashedPassword);

        return ResponseEntity.status(200).body(new UserResponse(200, "OK", "Sign up successfully"));

    }

    @Operation(summary = "Log in if existed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Log in successfully"),
            @ApiResponse(responseCode = "400", description = "Password or username invalid"),
            @ApiResponse(responseCode = "400", description = "Password or username invalid"),
            @ApiResponse(responseCode = "400", description = "All fields need to be filled")
    })
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody UserSignInDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (username == null || password == null) {
            return ResponseEntity.status(400).body(new UserResponse(400, "Bad request", "All fields need to be filled"));

        }

        User user = userService.findUser(username);
        if (user == null) {
            return ResponseEntity.status(400).body(new UserResponse(400, "Bad request", "Password or username invalid"));
        }

        boolean isPasswordMatch = BCrypt.checkpw(password, user.getPassword());

        if (!isPasswordMatch) {
            return ResponseEntity.status(400).body(new UserResponse(400, "Bad request", "Password or username invalid"));

        }
        String token = JwtProvider.generateToken(user);
        return ResponseEntity.status(200).body(new UserResponse(200, "Login successfully", token));

    }
}
// xoa lib thua, cmt
//return response
//sua ma thanh 400
//da ngon ngu -> header them language = en
//
//message, stack trace trong db
//send mail cho admin
