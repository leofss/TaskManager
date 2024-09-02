package com.leonardo.taskmanager.repository;

import com.leonardo.taskmanager.entity.Task;
import com.leonardo.taskmanager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUsers(User user, Pageable pageable);


    Page<Task> findByStatus(Task.Status status, Pageable pageable);
}
