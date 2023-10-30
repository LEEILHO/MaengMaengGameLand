package com.maeng.game.domain.room.service;

import com.maeng.game.domain.jwac.emums.Tier;
import com.maeng.game.domain.room.dto.CreateRoomDTO;
import com.maeng.game.domain.room.entity.Player;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.exception.NotFoundRoomException;
import com.maeng.game.domain.room.exception.PullRoomException;
import com.maeng.game.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepository roomRepository;

    // TODO : 대기방 생성
    public String createRoom(CreateRoomDTO createRoomDTO){

        String roomCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        roomRepository.save(Room.builder()
                        .id(roomCode)
                        .title(createRoomDTO.getTitle())
                        .createdAt(createRoomDTO.getCreatedAt())
                        .headCount(0)
                        .maxHeadCount(createRoomDTO.getHeadCount())
                        .publicRoom(createRoomDTO.isPublicRoom())
                        .participant(null)
                        .gameCategory(createRoomDTO.getGameCategory())
                .build());

        return roomCode;
    }

    // 대기방 입장
    public Room enterRoom(String roomCode, String nickname){

        // 방에 사람이 가득찼는지 확인하기
        Room roomInfo = roomRepository.findById(roomCode).orElse(null);
        if(roomInfo == null){
            log.info(roomCode);
            throw new NotFoundRoomException("존재하지 않는 방입니다.");
        }

        if(roomInfo.getHeadCount() == roomInfo.getMaxHeadCount()){
            throw new PullRoomException("플레이어가 가득 찬 방입니다.");
        }

        // 방이 가득차지 않았으면 headCount++ 후 Player 추가해주기
        int headCount = roomInfo.getHeadCount();
        List<Player> newList = new ArrayList<>();
        Player player = Player.builder()
                .nickname(nickname)
                .ready(false)
                .host(true)
                .profileUrl("넣어야 됨")
                .tier(Tier.GOLD) // TODO : 플레이어 정보 가져오기(프로필 사진, 티어)
                .build();

        if(roomInfo.getParticipant() != null) { // 방장이 아닐 경우
            newList.addAll(roomInfo.getParticipant());
            player.setHost(false);
        }

        newList.add(player);

        roomInfo.setParticipant(newList);
        roomInfo.setHeadCount(headCount+1);

        roomRepository.save(roomInfo);

        return roomInfo;
    }

    // TODO : sendMessageToUser - 특정 사용자에게 메세지 보낼 때


}
