package com.maeng.record.domain.record.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maeng.record.domain.record.entity.GameParticipant;
import com.maeng.record.domain.record.entity.GameUser;

@Repository
public interface GameParticipantRepository extends JpaRepository<GameParticipant, String> {
	Page<GameParticipant> findByGameUserOrderByGameStartAtDesc(GameUser gameUser, Pageable pageable);

	List<GameParticipant> findByGameGameCode(String gameCode);
}
