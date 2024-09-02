package com.leonardo.taskmanager.service;

import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.exception.EntityNotFoundExecption;
import com.leonardo.taskmanager.exception.UserNotAssignedToTaskException;
import com.leonardo.taskmanager.jwt.JwtUserDetailsService;
import com.leonardo.taskmanager.repository.TaskRepository;
import com.leonardo.taskmanager.repository.UserRepository;
import com.leonardo.taskmanager.web.dto.TaskDto;
import com.leonardo.taskmanager.web.dto.TaskResponseDto;
import com.leonardo.taskmanager.web.dto.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Transactional
    public Task create(TaskDto taskDto){
        List<Long> userIds = taskDto.getUserIds();
        Set<User> users = new HashSet<>(userRepository.findAllById(userIds));

        Task task = TaskMapper.toTaskEntity(taskDto);
        task.setUsers(users);
        task.setCreatedDate(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getTasksForUser(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Task> taskPage = taskRepository.findByUsers(user, pageable);

        return taskPage.map(TaskMapper::toTaskDtoResponse);
    }

    @Transactional
    public void delete(Long id){
        if(!taskRepository.existsById(id)){
            throw new EntityNotFoundExecption("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task edit(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundExecption("Task with id " + id + " not found"));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(taskDto, task);

        List<Long> userIds = taskDto.getUserIds();
        Set<User> users = new HashSet<>(userRepository.findAllById(userIds));

        checkIfUserIsAssignedToTask(userIds);
        task.setUsers(users);
        return taskRepository.save(task);
    }

    private void checkIfUserIsAssignedToTask(List<Long> userIds){
        Long currentUserLoggedInId = jwtUserDetailsService.getCurrentUserId();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))
                && !userIds.contains(currentUserLoggedInId)){
                throw new UserNotAssignedToTaskException("You must be assigned to this task in order to edit it");
        }

    }
}
