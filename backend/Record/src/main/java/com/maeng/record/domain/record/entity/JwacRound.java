package com.maeng.record.domain.record.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.maeng.record.domain.record.enums.Jewelry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwacRound {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jwacRoundSeq;

	@ManyToOne
	@JoinColumn(name = "game_code")
	private Game game;

	@ManyToOne
	@JoinColumn(name = "user_seq")
	private GameParticipant gameParticipant;

	@Column
	private Integer round;

	@Column
	@Enumerated(value = EnumType.STRING)
	private Jewelry jewelry;

	public void update(JwacRound jwacRound) {
		if(jwacRound.game != null)
			this.game = jwacRound.game;
		if(jwacRound.gameParticipant != null)
			this.gameParticipant = jwacRound.gameParticipant;
		if(jwacRound.round != null)
			this.round = jwacRound.round;
		if(jwacRound.jewelry != null)
			this.jewelry = jwacRound.jewelry;
	}
}
