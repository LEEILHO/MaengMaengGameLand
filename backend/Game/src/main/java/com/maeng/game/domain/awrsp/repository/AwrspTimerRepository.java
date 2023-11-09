package com.maeng.game.domain.awrsp.repository;

import com.maeng.game.domain.jwac.entity.Timer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwrspTimerRepository extends CrudRepository<Timer, String> {
}
