package com.leonardo.taskmanager.web.controller;

import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.exception.NoSearchParametersProvidedException;
import com.leonardo.taskmanager.service.TaskService;
import com.leonardo.taskmanager.web.api.TaskApi;
import com.leonardo.taskmanager.web.dto.*;
import com.leonardo.taskmanager.web.dto.mapper.TaskMapper;
import com.leonardo.taskmanager.web.dto.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class TaskController implements TaskApi {
    private final TaskService taskService;

    @Override
    public ResponseEntity<TaskResponseDto> create(TaskDto taskDto) {
        Task task = taskService.create(taskDto);
        return ResponseEntity.status(201).body(TaskMapper.toTaskDtoResponse(task));
    }

    @Override
    public Page<TaskResponseDto> getTasksForCurrentUser(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return taskService.getTasksFromLoggedInUser(username, pageable);
    }

    @Override
    public void delete(Long id) {
        taskService.delete(id);
    }

    @Override
    public ResponseEntity<TaskResponseDto> edit(Long id, TaskDto taskDto) {
        Task task = taskService.edit(id, taskDto);
        return ResponseEntity.ok().body(TaskMapper.toTaskDtoResponse(task));
    }

    @Override
    public Page<TaskResponseDto> searchTasks(String dueDate, String status, Pageable pageable) {
        if(status != null && !status.isEmpty()){
            return taskService.filterByStatus(status, pageable);
        } else if (dueDate != null) {
            return taskService.orderByDueDate(dueDate, pageable);
        }else{
            throw new NoSearchParametersProvidedException("At least one parameter ('status' or 'sort') must be provided");
        }
    }
}
