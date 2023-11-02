package com.maeng.record.domain.record.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwacRoundBid {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bidSeq;

	@ManyToOne
	@JoinColumn(name = "jwac_round_seq")
	private JwacRound jwacRound;

	@ManyToOne
	@JoinColumn(name = "user_seq")
	private GameUser gameUser;

	@Column
	private LocalDateTime bidAt;

	@Column
	private Long bidAmount;
}
