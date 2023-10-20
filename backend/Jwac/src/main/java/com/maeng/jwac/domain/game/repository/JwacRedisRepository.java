package com.maeng.jwac.domain.game.repository;

import org.springframework.data.repository.CrudRepository;

import com.maeng.jwac.domain.game.entity.Jwac;

public interface JwacRedisRepository extends CrudRepository<Jwac, String> {

}
