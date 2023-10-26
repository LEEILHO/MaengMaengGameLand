package com.maeng.game.domain.jwac.game.repository;

import org.springframework.data.repository.CrudRepository;

import com.maeng.game.domain.jwac.game.entity.Jwac;

public interface JwacRedisRepository extends CrudRepository<Jwac, String> {

}
