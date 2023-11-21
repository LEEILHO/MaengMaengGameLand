package com.maeng.record.domain.record.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GsbRound {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gsbRoundSeq;

	@ManyToOne
	@JoinColumn(name = "game_seq")
	private Game game;

	@ManyToOne
	@JoinColumn(name = "participant_seq")
	private GameParticipant roundWinner;

	@Column
	private Integer round;

	@Column
	private Integer chip;
}
