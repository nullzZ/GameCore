package com.game.server.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	public static List<?> random(List<?> list, int count) {
		int num = list.size() >= count ? count : list.size();
		List<Object> newList = new ArrayList<>(list);
		List<Object> retList = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			int ran = GameUtil.getRangedRandom(0, newList.size() - 1);
			Object obj = newList.remove(ran);
			retList.add(obj);
		}
		return retList;
	}

}
