package com.maeng.game.domain.awrsp.repository;

import com.maeng.game.domain.jwac.entity.Timer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends CrudRepository<Timer, String> {
    Timer findByGameCode(String gameCode);
}
