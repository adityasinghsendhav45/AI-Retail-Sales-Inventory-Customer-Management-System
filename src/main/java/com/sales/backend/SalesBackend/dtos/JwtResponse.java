package com.sales.backend.SalesBackend.dtos;


import com.sales.backend.SalesBackend.entities.User;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    UserDto user;
    //    private String jwtToken;
    private String refreshToken;
//    private RefreshTokenDto refreshToken;


}
