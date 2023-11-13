package com.maeng.record.domain.record.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.record.domain.record.entity.Mmj;

@Repository
public interface MmjRepository extends JpaRepository<Mmj, Long> {
	Optional<Mmj> findByGameUserEmail(String email);
}
