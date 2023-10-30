package com.maeng.game.domain.room.repository;

import com.maeng.game.domain.room.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoomRepository extends CrudRepository<Room, String> {
    Optional<Room> findById(String roomCode);
}
