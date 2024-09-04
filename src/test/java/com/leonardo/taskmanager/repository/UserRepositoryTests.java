package com.leonardo.taskmanager.repository;

import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.repository.projection.UserProjection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    void createUser(){
        User user1 = new User();
        user1.setUsername("leonardo");
        user1.setEmail("leo@gmail.com");
        user1.setPassword("12345");
        user1.setRole(User.Role.USER);
        user1.setCreatedDate(LocalDateTime.now());
        user1.setModifiedDate(LocalDateTime.now());
        user1.setCreatedBy("admin");
        user1.setLastModifiedBy("admin");
        userRepository.save(user1);
    }
    @Test
    void UserRepository_findAllPageable_ReturnPageUserProjetcion(){
        createUser();

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<UserProjection> userPage = userRepository.findAllPageable(pageRequest);
        assertFalse(userPage.isEmpty());
        assertEquals(1, userPage.getTotalElements());

        List<UserProjection> users = userPage.getContent();

        assertEquals("leonardo", users.get(0).getUsername());
        assertEquals("leo@gmail.com", users.get(0).getEmail());

        userRepository.deleteAll();
    }


    @Test
    void UserRepository_findByEmail_ReturnUserByEmail(){
        createUser();

        Optional<User> userRetrieved = userRepository.findByEmail("leo@gmail.com");

        assertTrue(userRetrieved.isPresent());
        assertEquals("leo@gmail.com", userRetrieved.get().getEmail());
        userRepository.deleteAll();
    }

    @Test
    void UserRepository_findRoleByEmail_ReturnRoleByEmail(){
        createUser();

        User.Role role = userRepository.findRoleByEmail("leo@gmail.com");

        assertEquals("USER", role.name());
        userRepository.deleteAll();
    }

    @Test
    void UserRepository_existsByRole_ReturnIfExistsByRole(){
        createUser();

        boolean exists = userRepository.existsByRole(User.Role.USER);

        assertTrue(exists);
        userRepository.deleteAll();
    }

    @Test
    void UserRepository_findByUsername_ReturnUserByUsername(){
        createUser();

        Optional<User> userRetrieved = userRepository.findByUsername("leonardo");


        assertTrue(userRetrieved.isPresent());
        assertEquals("leonardo", userRetrieved.get().getUsername());
        userRepository.deleteAll();
    }
}
