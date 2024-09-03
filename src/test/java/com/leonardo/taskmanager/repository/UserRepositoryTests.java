package com.leonardo.taskmanager.repository;

import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.repository.projection.UserProjection;
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

    @Test
    void UserRepository_findAllPageable_ReturnPageUserProjetcion(){
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

        User user2 = new User();
        user2.setUsername("maria");
        user2.setEmail("maria@gmail.com");
        user2.setPassword("54321");
        user2.setRole(User.Role.USER);
        user2.setCreatedDate(LocalDateTime.now());
        user2.setModifiedDate(LocalDateTime.now());
        user2.setCreatedBy("admin");
        user2.setLastModifiedBy("admin");

        userRepository.save(user2);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<UserProjection> userPage = userRepository.findAllPageable(pageRequest);
        assertFalse(userPage.isEmpty());
        assertEquals(2, userPage.getTotalElements());
        List<UserProjection> users = userPage.getContent();

        assertEquals("leonardo", users.get(0).getUsername());
        assertEquals("leo@gmail.com", users.get(0).getEmail());
        assertEquals("maria", users.get(1).getUsername());
        assertEquals("maria@gmail.com", users.get(1).getEmail());
    }


    @Test
    void UserRepository_findByEmail_ReturnUserByEmail(){
        User user = new User();
        user.setUsername("leonardo");
        user.setEmail("leo@gmail.com");
        user.setPassword("12345");
        user.setRole(User.Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setCreatedBy("admin");
        user.setLastModifiedBy("admin");

        userRepository.save(user);
        Optional<User> userRetrieved = userRepository.findByEmail(user.getEmail());

        assertTrue(userRetrieved.isPresent());
        assertEquals(user.getId(), userRetrieved.get().getId());
    }

    @Test
    void UserRepository_findRoleByEmail_ReturnRoleByEmail(){
        User user = new User();
        user.setUsername("leonardo");
        user.setEmail("leo@gmail.com");
        user.setPassword("12345");
        user.setRole(User.Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setCreatedBy("admin");
        user.setLastModifiedBy("admin");

        userRepository.save(user);
        User.Role role = userRepository.findRoleByEmail(user.getEmail());

        assertEquals(user.getRole().name(), role.name());
    }

    @Test
    void UserRepository_existsByRole_ReturnIfExistsByRole(){
        User user = new User();
        user.setUsername("leonardo");
        user.setEmail("leo@gmail.com");
        user.setPassword("12345");
        user.setRole(User.Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setCreatedBy("admin");
        user.setLastModifiedBy("admin");

        userRepository.save(user);
        boolean exists = userRepository.existsByRole(user.getRole());

        assertTrue(exists);
    }

    @Test
    void UserRepository_findByUsername_ReturnUserByUsername(){
        User user = new User();
        user.setUsername("leonardo");
        user.setEmail("leo@gmail.com");
        user.setPassword("12345");
        user.setRole(User.Role.USER);
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setCreatedBy("admin");
        user.setLastModifiedBy("admin");

        userRepository.save(user);
        Optional<User> userRetrieved = userRepository.findByUsername(user.getUsername());


        assertTrue(userRetrieved.isPresent());
        assertEquals(user.getId(), userRetrieved.get().getId());
    }
}
