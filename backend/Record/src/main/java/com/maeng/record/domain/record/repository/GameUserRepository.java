package com.maeng.record.domain.record.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.record.domain.record.entity.GameUser;

@Repository
public interface GameUserRepository extends JpaRepository<GameUser, String> {
	Optional<GameUser> findByNickname(String participant);

	Optional<GameUser> findByEmail(String email);

	boolean existsByEmail(String email);
}
