package com.leonardo.taskmanager.web.dto.mapper;

import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.web.dto.TaskDto;
import com.leonardo.taskmanager.web.dto.TaskResponseDto;
import org.modelmapper.ModelMapper;

public class TaskMapper {
    public static Task toTaskEntity(TaskDto taskDto){
        return new ModelMapper().map(taskDto, Task.class);
    }

    public static TaskResponseDto toTaskDtoResponse(Task task){
        return new ModelMapper().map(task, TaskResponseDto.class);
    }
}
