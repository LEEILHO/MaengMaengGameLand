package com.maeng.record.domain.record.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mmj {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mmjSeq;

	@OneToOne
	@JoinColumn(name = "game_user_seq")
	private GameUser gameUser;

	@Column
	private int score;

	@Column
	private LocalDateTime updatedAt;

	public void updateScore(int score) {
		this.score = score;
	}

	@PreUpdate
	public void update() {
		this.updatedAt = LocalDateTime.now();
	}

}
