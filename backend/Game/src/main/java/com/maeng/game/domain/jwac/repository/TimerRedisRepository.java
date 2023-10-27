package com.maeng.game.domain.jwac.repository;

import org.springframework.data.repository.CrudRepository;

import com.maeng.game.domain.jwac.entity.Timer;

public interface TimerRedisRepository extends CrudRepository<Timer, String> {

}
