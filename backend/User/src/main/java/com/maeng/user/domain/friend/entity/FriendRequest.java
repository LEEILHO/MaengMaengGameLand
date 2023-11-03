package com.maeng.user.domain.friend.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import org.hibernate.annotations.GenericGenerator;

import com.maeng.user.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "friend_request")
public class FriendRequest {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)")
	private UUID requestId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_seq")
	private User requester;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient_seq")
	private User recipient;

	@Column(name = "createAt")
	private LocalDateTime createAt;

	@PrePersist
	public void prePersist() {
		createAt = LocalDateTime.now();
	}
}
