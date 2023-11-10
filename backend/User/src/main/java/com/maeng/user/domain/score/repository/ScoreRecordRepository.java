package com.maeng.user.domain.score.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maeng.user.domain.score.entity.ScoreRecord;

public interface ScoreRecordRepository extends JpaRepository<ScoreRecord, Long> {
}
