package com.maeng.record.domain.record.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import com.maeng.record.domain.record.enums.GameCategoty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
	@Id
	private String gameCode;

	@Column
	@Enumerated(value = EnumType.STRING)
	private GameCategoty gameCategory;

	@Column
	private LocalDateTime startAt;

	@Column
	private LocalDateTime endAt;

	@PrePersist
	public void prePersist() {
		this.endAt = LocalDateTime.now();
	}
}
