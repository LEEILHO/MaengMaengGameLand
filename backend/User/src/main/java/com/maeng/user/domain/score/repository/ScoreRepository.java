package com.maeng.user.domain.score.repository;

import com.maeng.user.domain.score.entity.Score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

}
