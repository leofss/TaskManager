package com.leonardo.taskmanager.web.controller;

import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.exception.NoSearchParametersProvidedException;
import com.leonardo.taskmanager.service.TaskService;
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
@RequestMapping("api/v1/task")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TaskResponseDto> create(@RequestBody @Valid TaskDto taskDto) {
        Task task = taskService.create(taskDto);
        return ResponseEntity.status(201).body(TaskMapper.toTaskDtoResponse(task));
    }

    @GetMapping
    public Page<TaskResponseDto> getTasksForCurrentUser(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return taskService.getTasksForUser(username, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> edit(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.edit(id, taskDto);
        return ResponseEntity.ok().body(TaskMapper.toTaskDtoResponse(task));
    }

    @GetMapping("/search")
    public Page<TaskResponseDto> searchTasks(@RequestParam(required = false, name = "sort") String dueDate,
            @RequestParam(required = false,name = "status") String status, Pageable pageable){
        if(status != null && !status.isEmpty()){
            return taskService.filterByStatus(status, pageable);
        } else if (dueDate != null) {
            return taskService.orderByDueDate(dueDate, pageable);
        }else{
            throw new NoSearchParametersProvidedException("At least one parameter ('status' or 'sort') must be provided");
        }
    }
}
