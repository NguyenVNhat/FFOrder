package com.example.BE_PBL6_FastOrderSystem.repository;

import com.example.BE_PBL6_FastOrderSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    List<User> findAllByRole_Name(String roleName);

    Optional<User> findByEmail(String email);

    Optional<User> findByFacebookId(String facebookId);
    User findByid(Long id);
}