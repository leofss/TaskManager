package com.leonardo.taskmanager.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @JsonProperty("created_at")
    private LocalDateTime createdDate;

    @NotBlank
    @JsonProperty("due_date")
    private LocalDateTime dueDate;

    @NotBlank
    private Task.Status status;

    @NotBlank
    private Set<UserResponseDto> users;
}
