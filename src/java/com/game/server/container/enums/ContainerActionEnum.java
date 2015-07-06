package com.game.server.container.enums;

public enum ContainerActionEnum {

	INPUT(1), // 可放入物品
	OUTPUT(2), // 可以拿出物品
	MOVE(3), // 可以在本容器中移动
	DELETE(4);// 可以在本容器中删除
	private ContainerActionEnum(int type) {
		this.type = type;
	}

	private int type;

	public int getValue() {
		return type;
	}
}
