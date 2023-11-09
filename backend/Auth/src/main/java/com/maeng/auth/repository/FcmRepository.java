package com.maeng.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.auth.entity.Fcm;
import com.maeng.auth.entity.User;

@Repository
public interface FcmRepository extends JpaRepository<Fcm, Long> {
	Optional<Fcm> findByUser(User user);
}
