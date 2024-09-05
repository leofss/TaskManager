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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TaskServiceTests {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setup() {
        // Mock SecurityContext and Authentication
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(jwtUserDetailsService.getCurrentUserId()).thenReturn(1L);
        when(authentication.getName()).thenReturn("testUser");
    }

     User createUser(){
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@gmail.com");
        user.setPassword("12345");
        user.setRole(User.Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setCreatedBy("admin");
        user.setLastModifiedBy("admin");
        return user;
    }
    Task createTask(){
        User user = createUser();
        Set<User> userSet = new HashSet<>();
        userSet.add(user);
        Task task = new Task();
        task.setTitle("test");
        task.setDescription("test task");
        task.setDueDate(LocalDateTime.now());
        task.setStatus(Task.Status.CONCLUIDA);
        task.setCreatedDate(LocalDateTime.now());
        task.setUsers(userSet);
        return task;
    }

    @Test
    void TaskService_create_ReturnTask(){
        Task task = createTask();
        when(taskRepository.save(any(Task.class))).thenReturn(task);


        Task taskSaved = taskService.create(task);

        Assertions.assertThat(taskSaved).isNotNull();
        Assertions.assertThat(taskSaved.getTitle()).isEqualTo("test");
    }

    @Test
    void TaskService_getTasksFromLoggedInUser_ShouldReturnPageOfTaskResponseDto() {
        String username = "testuser";
        User user = createUser();
        Task task = createTask();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Task> taskPage = new PageImpl<>(List.of(task), pageable, 1);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(taskRepository.findByUsers(user, pageable)).thenReturn(taskPage);

        Page<TaskResponseDto> result = taskService.getTasksFromLoggedInUser(username, pageable);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent().get(0).getTitle()).isEqualTo("test");

        verify(userRepository, times(1)).findByUsername(username);
        verify(taskRepository, times(1)).findByUsers(user, pageable);
    }

    @Test
    void TaskService_getTasksFromLoggedInUser_ShouldThrowExceptionWhenUserNotFound() {
        String username = "nonexistentuser";
        Pageable pageable = PageRequest.of(0, 5);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTasksFromLoggedInUser(username, pageable))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findByUsername(username);
        verify(taskRepository, never()).findByUsers(any(), any());
    }

    @Test
    void TaskService_delete_ShouldDeleteTask_WhenTaskExists() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);

        taskService.delete(taskId);

        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void TaskService_delete_ShouldThrowException_WhenTaskDoesNotExist() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThatThrownBy(() -> taskService.delete(taskId))
                .isInstanceOf(EntityNotFoundExecption.class)
                .hasMessage("Task with id " + taskId + " not found");

        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void TaskService_edit_ShouldEditTask_WhenUserIsAssigned() {
        Task existingTask = createTask();
        List<Long> userIds = Collections.singletonList(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Updated Task");
        taskDto.setUserIds(userIds);

        when(taskRepository.findById(existingTask.getId())).thenReturn(Optional.of(existingTask));
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList()); // Non-admin user
        when(userRepository.findAllById(userIds)).thenReturn(List.of(createUser()));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task editedTask = taskService.edit(existingTask.getId(), taskDto);

        Assertions.assertThat(editedTask).isNotNull();
        Assertions.assertThat(editedTask.getTitle()).isEqualTo("Updated Task");
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void TaskService_edit_ShouldThrowException_WhenUserNotAssigned() {
        Task existingTask = createTask();
        List<Long> userIds = Collections.singletonList(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Updated Task");
        taskDto.setUserIds(userIds);

        when(taskRepository.findById(existingTask.getId())).thenReturn(Optional.of(existingTask));
        when(jwtUserDetailsService.getCurrentUserId()).thenReturn(999L); // User not assigned to task
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList()); // Non-admin user
        when(userRepository.findAllById(userIds)).thenReturn(List.of(createUser()));

        assertThatThrownBy(() -> taskService.edit(existingTask.getId(), taskDto))
                .isInstanceOf(UserNotAssignedToTaskException.class)
                .hasMessage("You must be assigned to this task in order to edit it");

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void TaskService_edit_ShouldThrowException_WhenTaskDoesNotExist() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.edit(taskId, taskDto))
                .isInstanceOf(EntityNotFoundExecption.class)
                .hasMessage("Task with id " + taskId + " not found");

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void TaskService_filterByStatus_ShouldReturnTasks_WhenStatusIsValid() {
        String status = "PENDENTE";
        Pageable pageable = Pageable.ofSize(10);

        Task task = new Task();
        task.setStatus(Task.Status.PENDENTE);

        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findByStatus(Task.Status.PENDENTE, pageable)).thenReturn(taskPage);

        Page<TaskResponseDto> result = taskService.filterByStatus(status, pageable);


        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent().get(0).getTitle()).isEqualTo(task.getTitle());

        verify(taskRepository, times(1)).findByStatus(Task.Status.PENDENTE, pageable);
    }
}
