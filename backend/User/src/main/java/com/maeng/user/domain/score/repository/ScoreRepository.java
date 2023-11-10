package com.maeng.user.domain.score.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maeng.user.domain.score.entity.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {
	List<Score> findByUser_NicknameIn(List<String> nicknames);
}
