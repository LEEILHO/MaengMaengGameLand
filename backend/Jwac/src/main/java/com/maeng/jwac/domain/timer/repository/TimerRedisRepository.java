package com.maeng.jwac.domain.timer.repository;

import org.springframework.data.repository.CrudRepository;

import com.maeng.jwac.domain.timer.entity.Timer;

public interface TimerRedisRepository extends CrudRepository<Timer, String> {

}
