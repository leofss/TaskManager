package com.leonardo.taskmanager.web.api;

import com.leonardo.taskmanager.jwt.JwtToken;
import com.leonardo.taskmanager.web.dto.UserLoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1")
@Tag(name = "V1 - Authentication")
public interface AuthApi {

    @PostMapping("/auth")
    @Operation(summary = "Login", description = "Generate JWT token valid for 30 minutes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users sucessfully logged in"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity"),
    })
    ResponseEntity<JwtToken> authenticate(@RequestBody @Valid UserLoginDto userLoginDto);
}
