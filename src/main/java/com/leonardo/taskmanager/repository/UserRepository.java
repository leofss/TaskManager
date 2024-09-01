package com.leonardo.taskmanager.repository;

import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.repository.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select c from User c")
    Page<UserProjection> findAllPageable(Pageable pageable);
}
