package com.leonardo.taskmanager.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @JsonProperty("due_date")
    private LocalDateTime dueDate;

    @NotBlank
    @Pattern(regexp = "PENDENTE|EM_ANDAMENTO|CONCLUIDA", message = "Status must be one of: EM_ANDAMENTO, PENDENTE, CONCLUIDA")
    private String status;

    @NotNull
    @JsonProperty("users_ids")
    private List<Long> userIds;
}
