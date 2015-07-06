package com.game.server.container;

public enum ContainerResultState {

	insert(1), update(2), delete(3),init(4);
	private int value;

	private ContainerResultState(int va) {
		value = va;
	}

	public int getValue() {
		return value;
	}
}
