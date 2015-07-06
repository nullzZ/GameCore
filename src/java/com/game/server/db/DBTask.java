package com.game.server.db;

public class DBTask implements Runnable {

	private String key;
	private Object data;
	private DBType type;

	public DBTask(String key, DBType type, Object data) {
		this.key = key;
		this.type = type;
		this.data = data;
	}

	public void run() {
		try {
			switch (type) {
			case insert:
				BaseDao.getInstance().insert(key, data);
				break;
			case update:
				BaseDao.getInstance().update(key, data);
				break;
			case delete:
				BaseDao.getInstance().delete(key, data);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getKey() {
		return key;
	}

	public Object getData() {
		return data;
	}

	public DBType getType() {
		return type;
	}

}
