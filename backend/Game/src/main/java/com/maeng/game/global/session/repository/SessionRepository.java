package com.maeng.game.global.session.repository;

import com.maeng.game.global.session.entity.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {
    Session findBySessionId(String sessionId);
}
