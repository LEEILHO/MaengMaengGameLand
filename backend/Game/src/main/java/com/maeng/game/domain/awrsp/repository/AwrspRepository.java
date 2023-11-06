package com.maeng.game.domain.awrsp.repository;

import com.maeng.game.domain.awrsp.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AwrspRepository extends CrudRepository<Game, String> {

    Optional<Game> findById(String gameCode);

}
