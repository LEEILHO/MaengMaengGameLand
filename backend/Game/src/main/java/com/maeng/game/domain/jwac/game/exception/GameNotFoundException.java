package com.maeng.game.domain.jwac.game.exception;

public class GameNotFoundException extends RuntimeException {
	public GameNotFoundException(String gameCode) {
		super("게임을 찾을 수 없습니다. gameCode : " + gameCode);
	}
}
