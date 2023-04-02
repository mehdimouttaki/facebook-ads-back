package com.example.adsfacebookads.mapper;

import com.example.adsfacebookads.dto.UserDTO;
import com.example.adsfacebookads.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOMapper extends GenericMapper<User, UserDTO> {

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "roles", expression = "java(mapRoles(source))")
    UserDTO sourceToTarget(User source) throws Exception, IllegalAccessException;

    @Override
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "roles", ignore = true)
    User targetToSource(UserDTO target);

    default List<String> mapRoles(User source) {
        if (!CollectionUtils.isEmpty(source.getRoles())) {
            List<String> roles = new ArrayList<>();
            source.getRoles().forEach(role -> {
                roles.add(role.getName());
            });

            return roles;
        } else {
            return new ArrayList<>();
        }
    }
}