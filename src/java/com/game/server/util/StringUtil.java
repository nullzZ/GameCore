package com.game.server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class StringUtil {

	private static final Logger logger = Logger.getLogger(StringUtil.class);

	// 判断一段文字是否为中文
	public static final boolean isChinese(String strName) {
		for (int i = 0; i < strName.length(); i++) {
			if (strName.substring(i, i + 1).matches("[\u4e00-\u9fa5]+")) {

			} else {
				return false;
			}

		}
		return true;
	}

	// 判断字符串是否有数字或字母组成
	public static final boolean stringIsNumLetters(String stringData) {
		boolean isDorL = true;
		for (int i = 0; i < stringData.length(); i++) {
			if (!((stringData.charAt(i) >= '0' && stringData.charAt(i) <= '9')
					|| (stringData.charAt(i) >= 'a' && stringData.charAt(i) <= 'z') || (stringData
					.charAt(i) >= 'A' && stringData.charAt(i) <= 'Z'))) {
				isDorL = false;
				break;
			}
		}
		return isDorL;
	}

	// 判断字符串是否全是数字
	public static final boolean stringIsNum(String stringData) {
		boolean isDorL = true;
		for (int i = 0; i < stringData.length(); i++) {
			if (!(stringData.charAt(i) >= '0' && stringData.charAt(i) <= '9')) {
				isDorL = false;
				break;
			}
		}
		return isDorL;
	}

	// 判断是否有特殊字符
	public static final boolean isConSpeCharacters(String string) {
		// TODO Auto-generated method stub
		if (string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*", "")
				.length() == 0) {
			// 如果不包含特殊字符
			return false;
		}
		return true;
	}

	/**
	 * 合并list内的字符串 格式：A|B|C
	 * 
	 * @param list
	 * @return
	 */
	public static final String merage(List<?> list) {

		if (list.size() <= 0)
			return "";

		StringBuilder sb = new StringBuilder();
		for (Object object : list) {
			sb.append(object);
			sb.append("|");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 拆分字符串 格式A|B|C
	 * 
	 * @param mstring
	 * @return
	 */
	public static final String[] split(String mstring) {
		if (mstring == null || mstring.length() <= 0)
			return null;

		return mstring.split("[|]");
	}

	/**
	 * 合并map的字符串 格式：key=value|key2=valu2|key3=value3
	 * 
	 * @param mstring
	 * @param map
	 * @return
	 */
	public static final String merage(Map<String, String> map) {
		ArrayList<String> list = new ArrayList<String>();
		Iterator<?> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			StringBuilder sb = new StringBuilder();
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();

			sb.append(key);
			sb.append("=");
			sb.append(val);

			list.add(sb.toString());
		}
		return merage(list);
	}

	/**
	 * 切分字符串至map 格式：key=value|key2=valu2|key3=value3
	 * 
	 * @param mstring
	 * @return
	 */
	public static final HashMap<String, String> splitToMap(String mstring) {
		HashMap<String, String> map = new HashMap<String, String>();
		String[] array = split(mstring);

		if (array == null)
			return map;

		for (String s : array) {

			int index = s.indexOf('=');
			if (index < 0) {
				logger.debug("这是什么情况--->" + s);
			}

			String key = s.substring(0, index);
			String val = s.substring(index + 1);
//			System.out.println(key + "---" + val);
			map.put(key, val);

		}

		return map;
	}

	public static void main(String[] args) {

		// splitToMap("aaad=bb");

		// System.out.println("--" + 10/3);

		// HashMap map = new HashMap();
		// map.put("a,a", "1,1");
		// map.put("bb", "22");
		// map.put("cc", "3>3");
		// System.out.println("合并");
		// String mstring = merage(map);
		// System.out.println(mstring);
		// System.out.println("拆分");
		//
		// String [] arr = split(mstring);
		//
		// for (String string : arr) {
		// System.out.println(string);
		// }
		// System.out.println("tomap");
		// HashMap mp001 = splitToMap(mstring);
		// Iterator iter = mp001.entrySet().iterator();
		// while(iter.hasNext()){
		// Map.Entry<String, String> e = (Map.Entry<String, String>)iter.next();
		// System.out.println(e.getKey() +"->"+ e.getValue());
		// }

	}
}
