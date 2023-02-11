package com.example.adsfacebookads.mapper;

import com.example.adsfacebookads.dto.UserRequest;
import com.example.adsfacebookads.entity.Role;
import com.example.adsfacebookads.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserRequestMapper extends GenericMapper<User, UserRequest> {

    @Override
    @Mapping(target = "roles", ignore = true)
    UserRequest sourceToTarget(User source) throws Exception;

    @Override
    @Mapping(target = "roles", expression = "java(mapRoles(target))")
    User targetToSource(UserRequest target);


    default Set<Role> mapRoles(UserRequest target) {
        if (!CollectionUtils.isEmpty(target.getRoles())) {
            Set<Role> roles = new HashSet<>();
            target.getRoles().forEach(roleId -> {
                Role role = new Role();
                role.setId(roleId);
                roles.add(role);
            });

            return roles;
        } else {
            return new HashSet<>();
        }
    }
}
