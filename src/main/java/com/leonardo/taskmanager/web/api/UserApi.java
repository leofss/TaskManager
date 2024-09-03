package com.leonardo.taskmanager.web.api;

import com.leonardo.taskmanager.web.dto.PageableDto;
import com.leonardo.taskmanager.web.dto.TaskResponseDto;
import com.leonardo.taskmanager.web.dto.UserDto;
import com.leonardo.taskmanager.web.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/user")
@Tag(name = "V1 - User")
public interface UserApi {
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create user", description = "Available only for users with ADMIN role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "422", description = "Processable entity"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
            @ApiResponse(responseCode = "409", description = "User with email already exists"),

    })
    ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserDto userDto);

    @GetMapping
    @Operation(summary = "List all users", description = "Available for all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully listed"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
    })
    ResponseEntity<PageableDto> getAll(Pageable pageable);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete single user", description = "Available only for users with ADMIN role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    void delete(@PathVariable Long id);

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Edit single user", description = "Available only for users with ADMIN role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully edited"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
            @ApiResponse(responseCode = "409", description = "User with email already exists"),

    })
    ResponseEntity<UserResponseDto> edit(@PathVariable Long id, @Valid @RequestBody UserDto userDto);

    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get tasks by user id", description = "Available for all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks successfully listed"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    Page<TaskResponseDto> getTasksByUserId(@PathVariable Long id, Pageable pageable);
}
