package com.game.server.container;

/**
 * 容器抽象物品
 * 
 * @author nullzZ
 * 
 */
public abstract class AbstractContainerItem {

	protected int id;// 配置ID
	protected int containerId;
	protected int gridId = -1;// 格子ID
	protected int number = 1;// 0代表删除
	protected int maxNum = 1;
	protected boolean lockSate = false;// 是否锁定
	protected int equipment_class;
	protected boolean bind;// 是否绑定

	public AbstractContainerItem(int id, int equipment_class, int maxNum) {
		this.id = id;
		this.maxNum = maxNum;
		this.equipment_class = equipment_class;
	}

	public int getId() {
		return id;
	}

	public abstract long getRoleId();

	public abstract long getuId();

	public int getContainerId() {
		return containerId;
	}

	public int getGridId() {
		return gridId;
	}

	public int getNumber() {
		return number;
	}

	public boolean isLockSate() {
		return lockSate;
	}

	public boolean isBind() {
		return bind;
	}

	public abstract AbstractContainerItem split();

	public abstract void setUid(long uid);

	public abstract void setContainerId(int containerId);

	// public abstract int getGridId();

	public abstract void setGridId(int gridId);

	// public abstract int getNumber();

	public abstract void setNumber(int number);

	// public abstract boolean isBind();

	public abstract void setBind(boolean bind);

	// public abstract boolean isLockSate();

	public abstract void setLockSate(boolean lockSate);

	public boolean addSum(int s) {
		int value = getNumber() + s;
		if (value <= 0) {
			return false;
		} else if (value > this.maxNum) {
			return false;
		}
		setNumber(value);
		return true;
	}

}
