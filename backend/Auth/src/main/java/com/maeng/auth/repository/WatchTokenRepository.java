package com.maeng.auth.repository;

import com.maeng.auth.entity.WatchJwtRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WatchTokenRepository extends CrudRepository<WatchJwtRedis, String> {
    Optional<WatchJwtRedis> findByEmail(String email);


}
