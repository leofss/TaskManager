package com.leonardo.taskmanager.web.dto.mapper;

import com.leonardo.taskmanager.entity.User;
import com.leonardo.taskmanager.web.dto.UserDto;
import com.leonardo.taskmanager.web.dto.UserResponseDto;
import org.modelmapper.ModelMapper;

public class UserMapper {
    public static User toUserEntity(UserDto dto){
        return new ModelMapper().map(dto, User.class);
    }

    public static UserResponseDto toUserDtoResponse(User user){
        return new ModelMapper().map(user, UserResponseDto.class);
    }
}
