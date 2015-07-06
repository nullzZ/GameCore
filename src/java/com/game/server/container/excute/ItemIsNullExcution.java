package com.game.server.container.excute;

public class ItemIsNullExcution extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7390621632638416965L;

	public ItemIsNullExcution(int gridId) {
		super("gridId:" + gridId + "is null");
	}

	public ItemIsNullExcution() {

	}
}
