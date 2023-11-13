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

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwrspRound {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long awrspRoundSeq;

	@ManyToOne
	@JoinColumn(name = "game_code")
	private Game game;

	@ManyToOne
	@JoinColumn(name = "user_seq")
	private GameUser gameUser;

	@Column
	private Integer round;

	@Column
	private Integer win;

	@Column
	private Integer draw;
}
