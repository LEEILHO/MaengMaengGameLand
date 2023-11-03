package com.maeng.auth.repository;

import com.maeng.auth.entity.WatchRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WatchRepository extends CrudRepository<WatchRedis , String> {

    Optional<WatchRedis> findByCode(String code);
}
