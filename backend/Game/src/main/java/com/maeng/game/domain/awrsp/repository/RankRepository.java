package com.maeng.game.domain.awrsp.repository;

import com.maeng.game.domain.awrsp.entity.Rank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends CrudRepository<Rank, String> {
}
