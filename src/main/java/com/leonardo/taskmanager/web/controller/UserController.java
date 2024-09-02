package com.leonardo.taskmanager.web.controller;

import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.repository.projection.UserProjection;
import com.leonardo.taskmanager.service.UserService;
import com.leonardo.taskmanager.web.dto.PageableDto;
import com.leonardo.taskmanager.web.dto.TaskResponseDto;
import com.leonardo.taskmanager.web.dto.UserDto;
import com.leonardo.taskmanager.web.dto.UserResponseDto;
import com.leonardo.taskmanager.web.dto.mapper.PageableMapper;
import com.leonardo.taskmanager.web.dto.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserDto userDto) {
        User user = UserMapper.toUserEntity(userDto);
        userService.create(user);
        return ResponseEntity.status(201).body(UserMapper.toUserDtoResponse(user));
    }

    @GetMapping
    public ResponseEntity<PageableDto> getAll(Pageable pageable) {
        Page<UserProjection> user = userService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.pageableDto(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> edit(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        User user = userService.edit(id, userDto);
        return ResponseEntity.ok().body(UserMapper.toUserDtoResponse(user));
    }

    @GetMapping("/{id}/tasks")
    public Page<TaskResponseDto> getTasksByUserId(@PathVariable Long id,Pageable pageable) {
        return userService.findTasksByUserId(id, pageable);
    }
}
