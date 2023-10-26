package com.maeng.game.domain.timer.repository;

import org.springframework.data.repository.CrudRepository;

import com.maeng.game.domain.timer.entity.Timer;

public interface TimerRedisRepository extends CrudRepository<Timer, String> {

}
