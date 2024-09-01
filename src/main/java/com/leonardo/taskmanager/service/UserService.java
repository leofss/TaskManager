package com.leonardo.taskmanager.service;

import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.exception.EmailUniqueViolationException;
import com.leonardo.taskmanager.exception.EntityNotFoundExecption;
import com.leonardo.taskmanager.repository.UserRepository;
import com.leonardo.taskmanager.repository.projection.UserProjection;
import com.leonardo.taskmanager.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(User user){
        checkIfEmailExists(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserProjection> findAll(Pageable pageable){
        return userRepository.findAllPageable(pageable);
    }

    @Transactional
    public void delete(Long id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundExecption("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public User edit(Long id, UserDto userDto) {
        checkIfEmailExists(userDto.getEmail());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundExecption("User with id " + id + " not found"));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(userDto, user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundExecption("User with email " + email + " not found"));
    }

    @Transactional(readOnly = true)
    public User.Role findRoleByEmail(String email){
        return userRepository.findRoleByEmail(email);
    }

    private void checkIfEmailExists(String email){
        if(userRepository.existsByEmail(email)){
            throw new EmailUniqueViolationException(String.format("User with Email %s already exists", email));
        }
    }
}
