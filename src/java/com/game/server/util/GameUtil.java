package com.game.server.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 随机数，日期工具等 随机数结果范围参考数学集合开闭区间规范
 * 
 * @author nullzZ
 * 
 */
public class GameUtil {
	private final static Logger logger = LoggerFactory
			.getLogger(GameUtil.class);

	// 返回[1-n]间的一个随机整数
	private static int getRandomOneToN(int n) {
		double tmp = Math.ceil(Math.random() * n);
		return (int) tmp;
	}

	// 返回 [0-n) 中不重复的m个数，主要用于实现 从某个长度为n的数组中随机得到m个元素组成的新数组
	public static List<Integer> getRandomMFromN(int n, int m) {
		List<Integer> base = new ArrayList<Integer>();
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i <= n; i++) {
			base.add(i, i);
		}
		int endpos = n > m ? m : n;
		for (int j = 0; j < endpos; j++) {
			int size = base.size();
			double tmp = Math.ceil(Math.random() * (size - 1));
			int pos = (int) tmp;
			result.add(base.get(pos) - 1);
			base.remove(pos);
		}
		return result;

	}

	/**
	 * 返回[min,max]间随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRangedRandom(int min, int max) {
		if (min == max) {
			return min;
		}
		min = min - 1;
		max = max - min;
		return GameUtil.getRandomOneToN(max) + min;
	}

	/**
	 * 计算百分比(保留1位并四舍五入)
	 * 
	 * @param area
	 *            分子
	 * @param total
	 *            分母
	 * @return
	 */
	public static double calcPercent(double area, double total) {
		double result;
		if (area == 0 || total == 0) {
			result = 0;
		} else {
			result = area / total;
		}
		int scale = 1;// 设置位数
		int roundingMode = 4;// 表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
		BigDecimal bd = new BigDecimal(result * 100);
		bd = bd.setScale(scale, roundingMode);
		result = bd.doubleValue();
		return result;
	}

	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}

	/**
	 * 快速完成时金币和世间的兑换比例
	 * 
	 * @param t
	 * @return
	 */
	public static long quickTime2Currency(long t) {

		if (t > 0) {
			if (t <= 180)
				return 1;
			else if (t <= 600) {
				return quickTime2Currency(180) + (t - 180) / 210
						+ ((t - 180) % 210 == 0 ? 0 : 1);
			} else if (t <= 3600) {
				return quickTime2Currency(600) + (t - 600) / 600
						+ ((t - 600) % 600 == 0 ? 0 : 1);
			} else if (t <= 21600) {
				return quickTime2Currency(3600) + (t - 3600) / 900
						+ ((t - 3600) % 900 == 0 ? 0 : 1);
			} else if (t <= 43200) {
				return quickTime2Currency(21600) + (t - 21600) / 1200
						+ ((t - 21600) % 1200 == 0 ? 0 : 1);
			} else if (t <= 86400) {
				return quickTime2Currency(43200) + (t - 43200) / 1500
						+ ((t - 43200) % 1500 == 0 ? 0 : 1);
			} else
				return quickTime2Currency(86400) + (t - 86400) / 1800
						+ ((t - 86400) % 1800 == 0 ? 0 : 1);
		}
		return 0;

	}

	/**
	 * 将一个Map的序列化字符串反序列化为一个Map
	 * 
	 * @param str
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static public Map<?, ?> stringToMap(String str, Class key, Class value) {
		Map map = new HashMap();
		try {
			if (str != null && !"".equals(str)) {
				String[] tmpList = str.split("\\|");
				for (String tmpKv : tmpList) {
					String[] tmp = tmpKv.split(":");
					try {
						map.put(String2OtherType(tmp[0], key),
								String2OtherType(tmp[1], value));
					} catch (Exception ex) {
						logger.error("[" + str + "] [" + key.getSimpleName()
								+ "] [" + value.getSimpleName() + "] [error]",
								ex);
					}
				}
			}
		} catch (Exception ex) {
			logger.error("[" + str + "] [" + key.getSimpleName() + "] ["
					+ value.getSimpleName() + "] [error]", ex);
		}
		return map;
	}

	static public Map<?, ?> stringToMap(String str, Object key, Object value) {
		return stringToMap(str, key.getClass(), value.getClass());
	}

	/**
	 * 将一个list的序列化字符串反序列化为一个list
	 * 
	 * @param str
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public List<?> stringToList(String str, Class key) {
		List list = new ArrayList();
		if (str != null && !"".equals(str)) {
			String[] tmpList = str.split("\\|");
			for (String tmp : tmpList) {
				try {
					list.add(String2OtherType(tmp, key));
				} catch (Exception ex) {
					logger.error("[" + str + "] [" + key.getSimpleName()
							+ "] [error]", ex);
				}
			}
		}
		return list;
	}

	/**
	 * 将一个String的字符转换为其他数据类型
	 * 
	 * @param str
	 * @param t
	 *            <Integer,String,Float等数据类型>
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public Object String2OtherType(String str, Class t) throws Exception {
		if (t == String.class) {
			return str;
		} else {
			try {
				Method method = t.getMethod("valueOf", String.class);
				return method.invoke(t, str);
			} catch (Exception ex) {
				logger.error("[" + str + "] [" + t.getSimpleName() + "]", ex);
				throw ex;
			}
		}
	}

	/**
	 * 将一个map序列化为一个字符串
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	static public String Map2String(Map map) {
		String str = "";
		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			String value = map.get(key).toString();
			str += key + ":" + value;
			if (iter.hasNext()) {
				str += "|";
			}
		}
		return str;
	}

	/**
	 * 将一个list序列化为一个字符串
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	static public String List2String(List list) {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				str += "|";
			}
			str += list.get(i);
		}
		return str;
	}

	public static boolean isLegalChar(String password) {
		return true;
	}

	/**
	 * 等级需要经验公式 3.75*n*n+3.75*n+1+0.5*(-1)^(n+1)
	 */
	public static int levelExp(int level) {
		int needExp = 0;
		BigDecimal num = new BigDecimal(-1);
		double m = num.pow(level + 1).intValue() * 0.5;
		needExp = (int) (3.75 * level * level + 3.75 * level + 1 + m);

		return needExp;
	}

	/**
	 * 返回参数总和的百分比命中率 返回随机到的索引
	 * 
	 * @param t
	 * @return
	 */
	public static int getRandomChoiceWithRatioArr(int... ratios) {
		int sum = 0;
		for (int i : ratios) {
			sum += i;
		}
		int r = getRandomOneToN(sum);
		int total = 0;
		for (int i = 0; i < ratios.length; i++) {
			total += ratios[i];
			if (r <= total) {
				return i;
			}
		}
		throw new RuntimeException("It can't be here!");
	}

	/**
	 * 获取俩天之间的差
	 * 
	 * @return
	 */
	public static int getDoubleDayCha(Date d1, Date d2) {
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long l1 = c.getTimeInMillis();
		c.setTime(d2);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long l2 = c.getTimeInMillis();
		return (int) ((l2 - l1) / (1000 * 60 * 60 * 24));
	}

	/**
	 * 是否同一天
	 * 
	 * @param dateA
	 * @param dateB
	 * @return
	 */
	public static boolean areSameDay(Date dateA, Date dateB) {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(dateA);

		Calendar calDateB = Calendar.getInstance();
		calDateB.setTime(dateB);

		return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
				&& calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
				&& calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
						.get(Calendar.DAY_OF_MONTH);
	}
}
