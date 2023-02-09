package com.example.adsfacebookads.mapper;

import com.example.adsfacebookads.dto.UserResponse;
import com.example.adsfacebookads.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface USerResponseMapper extends GenericMapper<User, UserResponse> {
    @Override
    @Mapping(source = "userId",target = "id")
    @Mapping(source = "username",target = "username")
    @Mapping(source = "password",target = "password")
    @Mapping(source = "firstName",target = "firstName")
    @Mapping(source = "lastName",target = "lastName")
    @Mapping(source = "email",target = "email")

    UserResponse sourceToTarget(User source) throws Exception, IllegalAccessException;

    @Override
    @Mapping(target = "userId",source = "id")
    @Mapping(target = "username",source = "username")
    @Mapping(target = "password",source = "password")
    @Mapping(target = "firstName",source = "firstName")
    @Mapping(target = "lastName",source = "lastName")
    @Mapping(target = "email",source = "email")
    User targetToSource(UserResponse target);
}
