package com.maeng.record.domain.record.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.maeng.record.domain.record.enums.Card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwrspRoundData {
	@Id
	private Long awrspRoundDataSeq;

	@ManyToOne
	@JoinColumn(name = "awrsp_round_seq")
	private AwrspRound awrspRound;

	@Column
	private Integer sequence;

	@Enumerated(value = EnumType.STRING)
	private Card card;
}
