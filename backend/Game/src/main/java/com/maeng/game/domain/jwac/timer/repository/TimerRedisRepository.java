package com.maeng.game.domain.jwac.timer.repository;

import org.springframework.data.repository.CrudRepository;

import com.maeng.game.domain.jwac.timer.entity.Timer;

public interface TimerRedisRepository extends CrudRepository<Timer, String> {

}
