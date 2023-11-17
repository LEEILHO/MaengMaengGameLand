package com.maeng.record.domain.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.record.domain.record.entity.AwrspRoundData;

@Repository
public interface AwrspRoundDataRepository extends JpaRepository<AwrspRoundData, Long> {
}
