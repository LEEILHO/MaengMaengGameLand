package com.maeng.game.domain.awrsp.repository;

import com.maeng.game.domain.awrsp.entity.Submit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmitRepository extends CrudRepository<Submit, String> {
    Optional<Submit> findById(String gameCode);
}
