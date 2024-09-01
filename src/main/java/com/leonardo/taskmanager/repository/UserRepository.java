package com.leonardo.taskmanager.repository;

import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.repository.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select c from User c")
    Page<UserProjection> findAllPageable(Pageable pageable);

    Optional<User> findByEmail(String email);

    @Query("select u.role from User u where u.email like :email")
    User.Role findRoleByEmail(String email);

    boolean existsByRole(User.Role role);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
}
