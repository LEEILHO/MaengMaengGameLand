package com.maeng.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
public class Fcm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fcmSeq;

	@OneToOne
	@JoinColumn(name = "userSeq")
	private User user;

	@Column
	private String fcmToken;
}
