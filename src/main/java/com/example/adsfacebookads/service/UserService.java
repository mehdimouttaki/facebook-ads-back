package com.example.adsfacebookads.service;


import com.example.adsfacebookads.dto.*;
import com.example.adsfacebookads.entity.Role;
import com.example.adsfacebookads.entity.SearchRequestValues;
import com.example.adsfacebookads.entity.User;
import com.example.adsfacebookads.exception.ResourceNotFoundException;
import com.example.adsfacebookads.mapper.USerResponseMapper;
import com.example.adsfacebookads.mapper.UserDTOMapper;
import com.example.adsfacebookads.mapper.UserRequestMapper;
import com.example.adsfacebookads.repository.RoleRepository;
import com.example.adsfacebookads.repository.UserRepository;
import com.example.adsfacebookads.utils.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    AuthenticationManager authenticationManager;
   private final UserRepository userRepository;


     private final BCryptPasswordEncoder bCryptPasswordEncoder;

     private final PasswordEncoder passwordEncoder ;


    private final RoleRepository roleRepository;



    private final UserRequestMapper userRequestMapper;

    private  final USerResponseMapper userResponseMapper;

    private final UserDTOMapper userDTOMapper;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRequestMapper userRequestMapper, USerResponseMapper userResponseMapper, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
        this.userDTOMapper = userDTOMapper;
    }

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User save(User user) {

        return userRepository.save(user);
    }

    public Page<User> findAllUsers(List<SearchRequest> searchRequests,Integer pageNumber , Integer pageSize) throws Exception {
        SearchRequestValues searchRequestValues=new SearchRequestValues();
        this.getSearchRequestsValues(searchRequests, searchRequestValues);
        PageRequest paging = PageRequest.of(pageNumber, pageSize);

        Page<User> pagedResult = userRepository.findAllByAdminId(searchRequestValues.getAdminId(), paging);
        Long count=userRepository.countUserRsp(searchRequestValues.getAdminId());
        SearchResponse<User> response=new SearchResponse<>();
        response.setSearchValue(pagedResult.getContent());
        response.setSearchCount(count);

       return pagedResult ;

    }

    private void getSearchRequestsValues(List<SearchRequest> searchRequestList, SearchRequestValues searchRequestValues)  {

        for (SearchRequest request : searchRequestList) {

            if (request.getField().equals("adminId")) {
                searchRequestValues.setAdminId(Long.valueOf(request.getValue()));


    }}}

    public ResponseEntity<User> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User Id : %d is not found", userId)));
        return ResponseEntity.ok().body(user);
    }

    public UserResponse createUser(UserRequest request) throws Exception {
        User user = userRequestMapper.targetToSource(request);
        if (Objects.nonNull(request.getAdminId())) {
            if (Objects.nonNull(request.getUsername())) {
                Optional<User> userWIthUsername = userRepository.findByUsernameIgnoreCase(request.getUsername());
                if (userWIthUsername.isPresent()) {
                    throw new ResourceNotFoundException(String.format("username name : %s is duplicated", user.getUsername()));

                }
            }

            if (Objects.nonNull(request.getEmail())) {
                Optional<User> userWithEmail = userRepository.findByEmailIgnoreCase(request.getEmail());
                if (userWithEmail.isPresent()) {
                    throw new ResourceNotFoundException(String.format("email : %s  is duplicated ", user.getEmail()));

                }
            }
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
            user.setRoles(new HashSet<>());
            for (Long roles : request.getRoles()) {
                Role role = roleRepository.findById(roles).orElseThrow(() -> new ResourceNotFoundException("Role with id " + roles + " Not Found"));
                user.getRoles().add(role);
            }
            user.setAdminId(request.getAdminId());
        }

        return userResponseMapper.sourceToTarget(userRepository.save(user));
    }

    public UserResponse updateUser(UserRequest userRequest, Long userId) throws Exception {
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User Id : %d is not found", userId)));
        if (Objects.isNull(userRequest.getPassword())) {
            User newUser = userRequestMapper.targetToSource(userRequest);
            if (Objects.nonNull(userRequest.getUsername())) {
                Optional<User> personWitUsernameDifId = userRepository.findByUsernameIgnoreCaseDifId(userRequest.getUsername(), userId);
                if (personWitUsernameDifId.isPresent()) {
                    throw new ResourceNotFoundException(String.format("this username : %s is duplicated ", userRequest.getUsername()));
                }
            }

            if (Objects.nonNull(userRequest.getEmail())) {
                Optional<User> personWitEmailDifId = userRepository.findByEmailIgnoreCaseDifId(userRequest.getEmail(), userId);
                if (personWitEmailDifId.isPresent()) {
                    throw new ResourceNotFoundException(String.format("this email : %s is duplicated ", userRequest.getEmail()));
                }
            }
            newUser.setRoles(new HashSet<>());
            userRequest.getRoles().forEach(roleId -> newUser.getRoles().add(roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role with id " + roleId + " Not Found"))));
            newUser.setUserId(userId);
            newUser.setPassword(oldUser.getPassword());
            return userResponseMapper.sourceToTarget(userRepository.save(newUser));
        }
        throw new ResourceNotFoundException(String.format("Password is not required in Modify user "));
    }

    public String deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User Id : %d is not found", userId)));
        userRepository.deleteById(user.getUserId());
        return "User id: " + userId + " is deleted.";
    }

    public UserDTO updatePassword(Long userId,UpdatePasswordRequest updatePasswordRequest) throws Exception {

        User byUserId = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(String.format("The User Id : %d : Not Found", userId)));

       Boolean encodeOldPassword = bCryptPasswordEncoder.matches(updatePasswordRequest.getOldPassword(), byUserId.getPassword());

       if (encodeOldPassword.equals(true)){
           if (updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getOldPassword())) {
               throw new ResourceNotFoundException("NEW PASSWORD IS THE SAME AS THE OLD ONE");
           } else if (updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
               byUserId.setPassword(bCryptPasswordEncoder.encode(updatePasswordRequest.getNewPassword()));
              return userDTOMapper.sourceToTarget(userRepository.save(byUserId));


           } else {
               throw new ResourceNotFoundException("NEW PASSWORD DOES NOT MATCH ITS CONFIRMATION");
           }
       }else {
           throw new RuntimeException("Wrong Password");
       }
    }


    public UserDTO changePassword(Long userId,String newPassword) throws Exception {

        User byUserId = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(String.format("The User Id : %d : Not Found", userId)));

        byUserId.setPassword(bCryptPasswordEncoder.encode(newPassword));

        return userDTOMapper.sourceToTarget(userRepository.save(byUserId));
    }


    public Long count() {
        return userRepository.count();
    }

    public UserResponse createAdmin(UserRequest request) throws Exception {
        User user = userRequestMapper.targetToSource(request);

        if (Objects.nonNull(request.getUsername())) {
            Optional<User> userWIthUsername = userRepository.findByUsernameIgnoreCase(request.getUsername());
            if (userWIthUsername.isPresent()) {
                throw new ResourceNotFoundException(String.format("username name : %s is duplicated", user.getUsername()));

            }
        }

        if (Objects.nonNull(request.getEmail())) {
            Optional<User> userWithEmail = userRepository.findByEmailIgnoreCase(request.getEmail());
            if (userWithEmail.isPresent()) {
                throw new ResourceNotFoundException(String.format("email : %s  is duplicated ", user.getEmail()));

            }
        }
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRoles(new HashSet<>());
        for (Long roles : request.getRoles()) {
            Role role = roleRepository.findById(roles).orElseThrow(() -> new ResourceNotFoundException("Role with id " + roles + " Not Found"));
            user.getRoles().add(role);
        }
        return userResponseMapper.sourceToTarget(userRepository.save(user));

    }
}





