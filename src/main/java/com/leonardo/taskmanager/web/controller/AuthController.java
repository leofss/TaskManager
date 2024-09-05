package com.leonardo.taskmanager.web.controller;

import com.leonardo.taskmanager.jwt.JwtToken;
import com.leonardo.taskmanager.jwt.JwtUserDetailsService;
import com.leonardo.taskmanager.web.api.AuthApi;
import com.leonardo.taskmanager.web.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1")
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;


    @Override
    public ResponseEntity<JwtToken> authenticate(UserLoginDto userLoginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword());
            authenticationManager.authenticate(authenticationToken);
            JwtToken token = jwtUserDetailsService.getTokenAuthenticate(userLoginDto.getEmail());
            return ResponseEntity.ok(token);

        }catch (AuthenticationException ex){
            throw new AccessDeniedException("Invalid credentials");
        }
    }
}
