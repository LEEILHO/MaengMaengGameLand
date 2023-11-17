package com.maeng.game.domain.gsb.repository;

import com.maeng.game.domain.gsb.entity.Timer;
import org.springframework.data.repository.CrudRepository;



public interface GsbTimerRepository extends CrudRepository<Timer,String> {
}
