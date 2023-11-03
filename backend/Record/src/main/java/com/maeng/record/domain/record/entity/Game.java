package com.maeng.record.domain.record.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.maeng.record.domain.record.enums.GameCategoty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
	@Id
	private String gameCode;

	@Column
	@Enumerated(value = EnumType.STRING)
	private GameCategoty gameCategory;
}
