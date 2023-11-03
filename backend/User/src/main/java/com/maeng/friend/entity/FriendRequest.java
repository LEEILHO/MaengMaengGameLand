package com.maeng.friend.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.maeng.user.entity.User;

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
	@Type(type = "uuid-char")
	@Column(name = "friendId", columnDefinition = "CHAR(36)")
	private String requsetId;

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
