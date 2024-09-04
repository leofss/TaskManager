package com.leonardo.taskmanager.repository;

import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.repository.projection.UserProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TaskRepositoryTests {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    private User createUser(){
        User user = new User();
        user.setUsername("teste");
        user.setEmail("test@gmail.com");
        user.setPassword("12345");
        user.setRole(User.Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setCreatedBy("admin");
        user.setLastModifiedBy("admin");
        return userRepository.save(user);
    }
    void createTask(){
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
        taskRepository.save(task);
    }

    @Test
    void TaskRepository_findByStatus_FindTasksByStatus(){
        createTask();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> completedTasks = taskRepository.findByStatus(Task.Status.CONCLUIDA, pageable);

        assertFalse(completedTasks.isEmpty());
        assertEquals(1, completedTasks.getTotalElements());
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void TaskRepository_findTasksByUserId_ReturnTaskByUserId(){
        createTask();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> tasksForUser = taskRepository.findTasksByUserId(1L, pageable);

        assertFalse(tasksForUser.isEmpty());

        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void TaskRepository_findAllTasksOrderedByDueDateDesc_ReturnTasksOrderedByDesc(){
        createTask();
        User user = new User();
        user.setUsername("teste 2");
        user.setEmail("test2@gmail.com");
        user.setPassword("12345");
        user.setRole(User.Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setCreatedBy("admin");
        user.setLastModifiedBy("admin");
        userRepository.save(user);

        Set<User> userSet = new HashSet<>();
        userSet.add(user);
        Task task = new Task();
        task.setTitle("test 2");
        task.setDescription("test task 2");
        task.setDueDate(LocalDateTime.now());
        task.setStatus(Task.Status.CONCLUIDA);
        task.setCreatedDate(LocalDateTime.now());
        task.setUsers(userSet);
        taskRepository.save(task);


        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> tasksPage = taskRepository.findAllTasksOrderedByDueDateDesc(pageable);

        List<Task> tasks = tasksPage.getContent();

        assertEquals(2, tasks.size());
        assertTrue(tasks.get(0).getDueDate().isAfter(tasks.get(1).getDueDate()));

        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

}
