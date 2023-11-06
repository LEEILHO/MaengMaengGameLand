package com.maeng.game.domain.awrsp.repository;

import com.maeng.game.domain.awrsp.entity.Submit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitRepository extends CrudRepository<Submit, String> {
    Submit findByGameCode(String gameCode);
}
