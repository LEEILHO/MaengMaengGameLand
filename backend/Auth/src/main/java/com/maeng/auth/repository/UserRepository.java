package com.maeng.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.auth.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
