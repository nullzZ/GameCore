package com.game.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

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

	public static boolean areSameDay(long timestamp1, long timestamp2) {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTimeInMillis(timestamp1);

		Calendar calDateB = Calendar.getInstance();
		calDateA.setTimeInMillis(timestamp2);

		return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
				&& calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
				&& calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
						.get(Calendar.DAY_OF_MONTH);
	}

	public static long getSurplusTime(int hour) {
		Calendar calDateA = Calendar.getInstance();
		long nextDay = getNextDayHour(calDateA.getTime(), hour);
		return nextDay - System.currentTimeMillis();
	}

	public static Date getNextDay(Date date) {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(date);
		calDateA.add(Calendar.DAY_OF_MONTH, 1);
		return calDateA.getTime();
	}

	/**
	 * 获取今天的日期 不包括时间
	 * 
	 * @return
	 */
	public static Date getToday() {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(new Date());
		calDateA.set(Calendar.HOUR_OF_DAY, 0);
		calDateA.set(Calendar.MINUTE, 0);
		calDateA.set(Calendar.SECOND, 0);
		calDateA.set(Calendar.MILLISECOND, 0);
		return calDateA.getTime();
	}

	// 下一天的某个时间
	public static long getNextDayHour(Date date, int hour) {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(date);
		calDateA.add(Calendar.DAY_OF_MONTH, 1);
		calDateA.set(Calendar.HOUR_OF_DAY, hour);
		calDateA.set(Calendar.MINUTE, 0);
		calDateA.set(Calendar.SECOND, 0);
		return calDateA.getTimeInMillis();
	}

	/**
	 * 获取今天的星期(周日是1)
	 * 
	 * @return
	 */
	public static int getTodayWeek() {
		Calendar calDateA = Calendar.getInstance();
		return calDateA.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 今天某一时刻毫秒数
	 * 
	 * @param nowTime
	 * @return
	 */
	public static long getTodayTime(String nowTime) {

		Calendar today = Calendar.getInstance();

		try {
			Date date1 = new SimpleDateFormat("HH:mm:ss").parse(nowTime);
			Calendar calDateA = Calendar.getInstance();
			calDateA.setTime(date1);
			calDateA.set(Calendar.YEAR, today.get(Calendar.YEAR));
			calDateA.set(Calendar.MONTH, today.get(Calendar.MONTH));
			calDateA.set(Calendar.DAY_OF_MONTH,
					today.get(Calendar.DAY_OF_MONTH));
			calDateA.set(Calendar.MILLISECOND, today.get(Calendar.MILLISECOND));

			return calDateA.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 当前的时间是否在俩个时间之间
	 * 
	 * @return
	 */
	public static boolean isInTime(String starTime, String endTime) {
		try {
			Calendar calDateC = Calendar.getInstance();
			int h = calDateC.get(Calendar.HOUR_OF_DAY);
			int m = calDateC.get(Calendar.MINUTE);

			Date date1 = new SimpleDateFormat("HH:mm:ss").parse(starTime);
			Calendar calDateA = Calendar.getInstance();
			calDateA.setTime(date1);
			calDateA.set(Calendar.YEAR, calDateC.get(Calendar.YEAR));
			calDateA.set(Calendar.MONTH, calDateC.get(Calendar.MONTH));
			calDateA.set(Calendar.DAY_OF_MONTH,
					calDateC.get(Calendar.DAY_OF_MONTH));
			calDateA.set(Calendar.MILLISECOND,
					calDateC.get(Calendar.MILLISECOND));
			int h1 = calDateA.get(Calendar.HOUR_OF_DAY);
			int m1 = calDateA.get(Calendar.MINUTE);

			Date date2 = new SimpleDateFormat("HH:mm:ss").parse(endTime);
			Calendar calDateB = Calendar.getInstance();
			calDateB.setTime(date2);
			calDateB.set(Calendar.YEAR, calDateC.get(Calendar.YEAR));
			calDateB.set(Calendar.MONTH, calDateC.get(Calendar.MONTH));
			calDateB.set(Calendar.DAY_OF_MONTH,
					calDateC.get(Calendar.DAY_OF_MONTH));
			calDateB.set(Calendar.MILLISECOND,
					calDateC.get(Calendar.MILLISECOND));
			int h2 = calDateB.get(Calendar.HOUR_OF_DAY);
			int m2 = calDateB.get(Calendar.MINUTE);

			if (calDateC.after(calDateA) && calDateC.before(calDateB)) {
				return true;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isInTime(String starTime) {
		try {
			Calendar calDateC = Calendar.getInstance();
			int h = calDateC.get(Calendar.HOUR_OF_DAY);
			int m = calDateC.get(Calendar.MINUTE);

			Date date1 = new SimpleDateFormat("HH:mm:ss").parse(starTime);
			Calendar calDateA = Calendar.getInstance();
			calDateA.setTime(date1);
			int h1 = calDateA.get(Calendar.HOUR_OF_DAY);
			int m1 = calDateA.get(Calendar.MINUTE);

			if (h >= h1 && m >= m1) {
				return true;
			}
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Date getDate(String date, String time) throws ParseException {
		Date d = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(date + " "
				+ time);
		return d;
	}

	public static Date getTime2Date(String time) throws ParseException {
		Date date1 = new SimpleDateFormat("HH:mm:ss").parse(time);
		Calendar today = Calendar.getInstance();
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(date1);
		calDateA.set(Calendar.YEAR, today.get(Calendar.YEAR));
		calDateA.set(Calendar.MONTH, today.get(Calendar.MONTH));
		calDateA.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
		return calDateA.getTime();
	}

	/**
	 * 把MM/dd/yyyy HH:mm:ss格式转化 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String dateChangeDate(String d) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = sdf.parse(d);
		String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return s;
	}

	public static String date2String(Date date, String format) {
		return new SimpleDateFormat(format).format(date).toString();
	}

	public static void main(String[] args) {
		Date date;
		try {
			date = new SimpleDateFormat("HH:mm:ss").parse("13:01:02");
			System.out.println(isInTime("17:02:00", "17:10:00"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
