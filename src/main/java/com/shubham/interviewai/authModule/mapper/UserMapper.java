package com.shubham.interviewai.authModule.mapper;

import com.shubham.interviewai.authModule.dto.UserDto;
import com.shubham.interviewai.authModule.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(UserDto userDto) {
        UserEntity user = new UserEntity();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public static UserDto toDto(UserEntity userEntity) {
        UserDto user = new UserDto();
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        return user;
    }
}
