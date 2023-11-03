package com.maeng.game.domain.jwac.repository;

import org.springframework.data.repository.CrudRepository;

import com.maeng.game.domain.jwac.entity.Jwac;

public interface JwacRedisRepository extends CrudRepository<Jwac, String> {

}
