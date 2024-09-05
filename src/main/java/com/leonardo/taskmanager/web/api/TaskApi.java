package com.leonardo.taskmanager.web.api;

import com.leonardo.taskmanager.web.dto.TaskDto;
import com.leonardo.taskmanager.web.dto.TaskResponseDto;
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

@RequestMapping("api/v1/task")
@Tag(name = "V1 - Tasks")
public interface TaskApi {
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create task", description = "Available only for users with ADMIN role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully created"),
            @ApiResponse(responseCode = "422", description = "Processable entity"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
    })
    ResponseEntity<TaskResponseDto> create(@RequestBody @Valid TaskDto taskDto);

    @GetMapping
    @Operation(summary = "List tasks from current logged in user", description = "Available only for all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully listed"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
    })
    Page<TaskResponseDto> getTasksForCurrentUser(Pageable pageable);


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete single task", description = "Available only for users with ADMIN role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
            @ApiResponse(responseCode = "404", description = "task not found"),
    })
    void delete(@PathVariable Long id);

    @PutMapping("/{id}")
    @Operation(summary = "Edit single task", description = "Available only for users with ADMIN role or if user is assigned to task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully edited"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),

    })
    ResponseEntity<TaskResponseDto> edit(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto);

    @GetMapping("/search")
    @Operation(summary = "List by filtering status or sorting dueDate",
            description = "Use search?sort=dueDate or search?=PENDENTE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully listed"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
            @ApiResponse(responseCode = "400", description = "At least one parameter ('status' or 'sort') must be provided"),
            @ApiResponse(responseCode = "403", description = "Invalid status or dueDate value"),
    })
    Page<TaskResponseDto> searchTasks(@RequestParam(required = false, name = "sort") String dueDate,
                                      @RequestParam(required = false,name = "status") String status, Pageable pageable);

}
