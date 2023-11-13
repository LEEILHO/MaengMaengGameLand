package com.maeng.record.domain.record.exception;

public class GameAlreadyExistException extends RuntimeException {
	public GameAlreadyExistException(String gameCode) {
		super("이미 존재하는 게임입니다: " + gameCode);
	}
}
