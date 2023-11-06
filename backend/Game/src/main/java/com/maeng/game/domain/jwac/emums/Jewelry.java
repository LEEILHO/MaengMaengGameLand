package com.maeng.game.domain.jwac.emums;

import lombok.Getter;

@Getter
public enum Jewelry {
	SPECIAL(0), RUBY(1), SAPPHIRE(2), EMERALD(3), DIAMOND(4);

	private int index;

	Jewelry(int index) {
		this.index = index;
	}

}
