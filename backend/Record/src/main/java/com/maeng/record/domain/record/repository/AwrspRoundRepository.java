package com.maeng.record.domain.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.record.domain.record.entity.AwrspRound;

@Repository
public interface AwrspRoundRepository extends JpaRepository<AwrspRound, Long> {
}
