package com.leonardo.taskmanager.repository;

import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUsers(User user, Pageable pageable);


    Page<Task> findByStatus(Task.Status status, Pageable pageable);

    @Query("SELECT t FROM Task t JOIN t.users u WHERE u.id = :userId")
    Page<Task> findTasksByUserId(Long userId, Pageable pageable);
}
