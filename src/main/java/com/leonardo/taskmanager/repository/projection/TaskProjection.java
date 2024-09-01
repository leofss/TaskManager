package com.leonardo.taskmanager.repository.projection;

import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.web.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.Set;

public interface TaskProjection {
    Long getId();

    String getTitle();

    String getDescription();

    LocalDateTime getCreatedDate();

    LocalDateTime getDueDate();

    Set<UserResponseDto> getUsers();

    Task.Status getStatus();
}
