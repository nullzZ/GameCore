package com.game.server.process;

public class MessageModel {

	/**
	 * 执行分配的线程
	 */
	private int prossesThreadId;
	/**
	 * 执行的时间 防止客户端发包过快
	 */
	private long actionTimestamp;

	public MessageModel(int prossesThreadId, long actionTimestamp) {
		this.prossesThreadId = prossesThreadId;
		this.actionTimestamp = actionTimestamp;
	}

	public int getProssesThreadId() {
		return prossesThreadId;
	}

	public long getActionTimestamp() {
		return actionTimestamp;
	}

	public void setActionTimestamp(long actionTimestamp) {
		this.actionTimestamp = actionTimestamp;
	}

}
