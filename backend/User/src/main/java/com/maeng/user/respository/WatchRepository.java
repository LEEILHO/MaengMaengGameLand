package com.maeng.user.respository;

import com.maeng.user.entity.WatchRedis;
import org.springframework.data.repository.CrudRepository;

public interface WatchRepository extends CrudRepository<WatchRedis, String> {

    WatchRedis findByCode(String code);

    void deleteByRefreshToken(String refreshToken);
}
