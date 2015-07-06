package com.game.server.container.log;

public class LogDebug {

	public static boolean isDebug = true;

	public static void debug(String content) {
		System.out.println("[容器]" + content);
	}

}
