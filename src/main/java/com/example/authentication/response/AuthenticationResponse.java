package com.example.authentication.response;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String username;
    private long expiresIn;
    private String token;
    private String type;
}