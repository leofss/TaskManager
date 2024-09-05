package com.leonardo.taskmanager.web.controller;

import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.repository.projection.UserProjection;
import com.leonardo.taskmanager.service.UserService;
import com.leonardo.taskmanager.web.api.UserApi;
import com.leonardo.taskmanager.web.dto.PageableDto;
import com.leonardo.taskmanager.web.dto.TaskResponseDto;
import com.leonardo.taskmanager.web.dto.UserDto;
import com.leonardo.taskmanager.web.dto.UserResponseDto;
import com.leonardo.taskmanager.web.dto.mapper.PageableMapper;
import com.leonardo.taskmanager.web.dto.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<UserResponseDto> create(UserDto userDto) {
        User user = UserMapper.toUserEntity(userDto);
        userService.create(user);
        return ResponseEntity.status(201).body(UserMapper.toUserDtoResponse(user));
    }

    @Override
    public ResponseEntity<PageableDto> getAll(Pageable pageable) {
        Page<UserProjection> user = userService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.pageableDto(user));
    }

    @Override
    public void delete(Long id) {
        userService.delete(id);
    }

    @Override
    public ResponseEntity<UserResponseDto> edit(Long id, UserDto userDto) {
        User user = userService.edit(id, userDto);
        return ResponseEntity.ok().body(UserMapper.toUserDtoResponse(user));
    }

    @Override
    public Page<TaskResponseDto> getTasksByUserId(Long id, Pageable pageable) {
        return userService.findTasksByUserId(id, pageable);
    }


}
