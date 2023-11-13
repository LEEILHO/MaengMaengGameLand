package com.maeng.game.domain.gsb.dto;

import com.maeng.game.domain.gsb.entity.Player;
import com.maeng.game.domain.gsb.entity.StartCard;
import com.maeng.game.domain.room.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartDto {

    private String gameCode;
    private String roomCode;
    private int currentRound;
    private int carryOverChips;
    private String currentPlayer;
    private StartCard[] startCards;
    private Map<Integer, Player> players;
    private List<User> participiants;
}
