package com.example.adsfacebookads.mapper;

import com.example.adsfacebookads.dto.UserRequest;
import com.example.adsfacebookads.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRequestMapper extends GenericMapper<User, UserRequest>{
}
