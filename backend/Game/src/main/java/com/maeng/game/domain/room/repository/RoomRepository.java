package com.maeng.game.domain.room.repository;

import com.maeng.game.domain.lobby.enums.ChannelTire;
import com.maeng.game.domain.room.entity.Game;
import com.maeng.game.domain.room.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoomRepository extends CrudRepository<Room, String> {
    Optional<Room> findById(String roomCode);
    List<Room> findAllByGameCategory(Game gameCategory);
    List<Room> findAllByGameCategoryAndChannelTire(Game game, ChannelTire channelTire);
}
