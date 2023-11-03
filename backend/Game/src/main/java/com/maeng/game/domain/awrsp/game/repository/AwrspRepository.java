package com.maeng.game.domain.awrsp.game.repository;

import com.maeng.game.domain.awrsp.game.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwrspRepository extends CrudRepository<Game, String> {

}
