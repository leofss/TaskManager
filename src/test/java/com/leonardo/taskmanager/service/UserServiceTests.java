package com.leonardo.taskmanager.service;

import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.exception.EmailUniqueViolationException;
import com.leonardo.taskmanager.exception.EntityNotFoundExecption;
import com.leonardo.taskmanager.repository.TaskRepository;
import com.leonardo.taskmanager.repository.UserRepository;
import com.leonardo.taskmanager.repository.projection.UserProjection;
import com.leonardo.taskmanager.web.dto.TaskResponseDto;
import com.leonardo.taskmanager.web.dto.UserDto;
import com.leonardo.taskmanager.web.dto.mapper.TaskMapper;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private UserService userService;

    User createUser(){
        User user = new User();
        user.setUsername("leonardo");
        user.setEmail("leo@gmail.com");
        user.setPassword("12345");
        user.setRole(User.Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setCreatedBy("admin");
        user.setLastModifiedBy("admin");
        return user;
    }



    @Test
    void UserService_create_ReturnUser(){
        User user = createUser();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User userSaved = userService.create(user);

        Assertions.assertThat(userSaved).isNotNull();
        Assertions.assertThat(userSaved.getUsername()).isEqualTo("leonardo");
        Assertions.assertThat(userSaved.getEmail()).isEqualTo("leo@gmail.com");
    }

    @Test
    void UserService_findAll_ReturnPageOfUserProjection() {
        Pageable pageable = PageRequest.of(0, 10);

        UserProjection userProjection1 = mock(UserProjection.class);
        UserProjection userProjection2 = mock(UserProjection.class);
        Page<UserProjection> userPage = new PageImpl<>(List.of(userProjection1, userProjection2), pageable, 2);

        when(userRepository.findAllPageable(pageable)).thenReturn(userPage);

        Page<UserProjection> result = userService.findAll(pageable);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(2);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getContent().get(0)).isEqualTo(userProjection1);
        Assertions.assertThat(result.getContent().get(1)).isEqualTo(userProjection2);
    }

    @Test
    void UserService_delete_RemovesUserAndClearsTasks() {
        User user = createUser();
        user.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");

        Set<Task> taskSet = new HashSet<>();
        taskSet.add(task);
        user.setTasks(taskSet);

        Set<User> userSet = new HashSet<>();
        userSet.add(user);
        task.setUsers(userSet);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.delete(1L);

        verify(taskRepository, times(1)).save(task);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).delete(user);

        assertTrue(user.getTasks().isEmpty());
        assertTrue(task.getUsers().isEmpty());
    }

    @Test
    void UserService_delete_ThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.delete(1L))
                .isInstanceOf(EntityNotFoundExecption.class)
                .hasMessageContaining("User with id 1 not found");

        verify(taskRepository, never()).save(any(Task.class));
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void UserService_edit_UpdatesUserSuccessfully() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("oldEmail@example.com");
        existingUser.setPassword("oldPassword");

        UserDto userDto = new UserDto();
        userDto.setEmail("newEmail@example.com");
        userDto.setPassword("newPassword");
        userDto.setUsername("newUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.edit(1L, userDto);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getUsername()).isEqualTo("newUsername");
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo("newEmail@example.com");
        Assertions.assertThat(updatedUser.getPassword()).isEqualTo("encodedPassword");

        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(existingUser);
    }

    @Test
    void UserService_edit_ThrowsExceptionWhenUserNotFound() {
        UserDto userDto = new UserDto();
        userDto.setEmail("newEmail@example.com");
        userDto.setPassword("newPassword");
        userDto.setUsername("newUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.edit(1L, userDto))
                .isInstanceOf(EntityNotFoundExecption.class)
                .hasMessageContaining("User with id 1 not found");

        verify(userRepository).findById(1L);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void UserService_edit_ThrowsExceptionWhenEmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existingEmail@example.com");

        UserDto userDto = new UserDto();
        userDto.setEmail("existingEmail@example.com");
        userDto.setPassword("newPassword");
        userDto.setUsername("newUsername");

        when(userRepository.findByEmail("existingEmail@example.com")).thenReturn(Optional.of(new User()));

        Assertions.assertThatThrownBy(() -> userService.edit(1L, userDto))
                .isInstanceOf(EmailUniqueViolationException.class)
                .hasMessageContaining("User with Email existingEmail@example.com already exists");

    }

    @Test
    void UserService_findByEmail_ReturnsUser_WhenUserExists() {
        User user = createUser(); // Assume createUser() sets email properly
        when(userRepository.findByEmail("leo@gmail.com")).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail("leo@gmail.com");

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo("leo@gmail.com");
    }

    @Test
    void UserService_findByEmail_ThrowsException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail("nonexistent@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.findByEmail("nonexistent@gmail.com"))
                .isInstanceOf(EntityNotFoundExecption.class)
                .hasMessageContaining("User with email nonexistent@gmail.com not found");
    }

    @Test
    void UserService_findRoleByEmail_ReturnsRole_WhenUserExists() {
        User user = createUser();
        user.setRole(User.Role.ADMIN); // Example role
        when(userRepository.findRoleByEmail("leo@gmail.com")).thenReturn(User.Role.ADMIN);

        User.Role role = userService.findRoleByEmail("leo@gmail.com");

        Assertions.assertThat(role).isEqualTo(User.Role.ADMIN);
    }


    @Test
    void UserService_findTasksByUserId_ReturnsTaskResponseDtoPage() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setDescription("Description 1");
        task.setCreatedDate(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(1));
        task.setUsers(Collections.singleton(user));

        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setTitle(task.getTitle());
        taskResponseDto.setDescription(task.getDescription());
        taskResponseDto.setCreatedDate(task.getCreatedDate());
        taskResponseDto.setDueDate(task.getDueDate());

        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task), pageable, 1);
        when(taskRepository.findTasksByUserId(userId, pageable)).thenReturn(taskPage);

        Page<Task> result = taskRepository.findTasksByUserId(userId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(taskResponseDto.getTitle(), result.getContent().get(0).getTitle());
    }


}
