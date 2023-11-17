package com.maeng.record.domain.record.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
public class AwrspGameAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long awrspGameAnswerSeq;

	@OneToOne
	@JoinColumn(name = "game_code")
	private Game game;

	@Column
	private Integer sequence;

	@Enumerated(value = EnumType.STRING)
	private Card card;
}
