package com.example.adsfacebookads.repository;

import com.example.adsfacebookads.dto.UserResponse;
import com.example.adsfacebookads.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select us from User us " +
            "where :adminId is null or (us.adminId)  =:adminId ")

    Page<User> findAllByAdminId(@Param("adminId") Long adminId, PageRequest paging);
    boolean existsByUsername(String username);

    @Query("select u from User u where  lower(u.username) = lower(:username)  " +
            "and u.id <> :userId ")
    Optional<User> findByUsernameIgnoreCaseDifId(@Param("username") String username,@Param("userId") Long userId);

    @Query("select u from User u where  lower(u.email) = lower(:email)  " +
            "and u.id <> :userId ")
    Optional<User> findByEmailIgnoreCaseDifId(@Param("email") String email, @Param("userId") Long userId);




    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByEmailIgnoreCase(String email);

    @Query("select u from User u where  lower(u.username) = lower(:username) ")
    User findByUsername(@Param("username") String username);

    @Query("select count(u.id) " +
            "from User u " +
            "where (:adminId is null or (u.adminId) =:adminId) "

    )
    Long countUserRsp(@Param("adminId") Long adminId);
}
