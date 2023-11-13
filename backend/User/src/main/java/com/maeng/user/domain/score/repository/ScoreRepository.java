package com.maeng.user.domain.score.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maeng.user.domain.score.entity.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {
	Optional<Score> findByUser_Nickname(String nickname);
}
