package com.maeng.user.domain.user.respository;

import com.maeng.user.domain.user.entity.WatchRedis;
import org.springframework.data.repository.CrudRepository;

public interface WatchRepository extends CrudRepository<WatchRedis, String> {

    WatchRedis findByCode(String code);

    void deleteByRefreshToken(String refreshToken);
}
