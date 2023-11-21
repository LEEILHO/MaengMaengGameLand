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

import com.maeng.record.domain.record.enums.WinDrawLose;

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
public class GsbRoundHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gsbRoundHistorySeq;

	@ManyToOne
	@JoinColumn(name = "participant_seq")
	private GameParticipant gameParticipant;

	@ManyToOne
	@JoinColumn(name = "gsb_round_seq")
	private GsbRound gsbRound;

	@Column
	private Integer gold;

	@Column
	private Integer silver;

	@Column
	private Integer bronze;

	@Enumerated(EnumType.STRING)
	private WinDrawLose winDrawLose;
}
