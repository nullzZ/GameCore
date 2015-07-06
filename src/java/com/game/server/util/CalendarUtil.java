package com.game.server.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
	
	public static final int DAY_MILLIS = 24*60*60*1000;
	public static final int DAY_HOUR = 60*60*1000;

	private static final SimpleDateFormat s_date_format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static final String getDate() {
		return s_date_format.format(new Date());
	}

	public static final String getDate(Date date) {
		return s_date_format.format(date);
	}

	public static final String getDateString() {
		String dt = "";
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		dt += (sf.format(date) + "年");

		sf = new SimpleDateFormat("MM");
		dt += (sf.format(date) + "月");

		sf = new SimpleDateFormat("dd");
		dt += (sf.format(date) + "日");

		sf = new SimpleDateFormat("HH");
		dt += (sf.format(date) + "时");

		sf = new SimpleDateFormat("mm");
		dt += (sf.format(date) + "分");

		sf = new SimpleDateFormat("ss");
		dt += (sf.format(date) + "秒");

		return dt;
	}
	
	
	public static final boolean isValidDate(Date d_start,Date d_end)
	{
		Date now = new Date();		
		if(now.after(d_start) && now.before(d_end))
			return true;
		return false;
	}
	public static final boolean isValidDate(Date d_end)
	{
		Date now = new Date();		
		if(now.before(d_end))
			return true;
		return false;
	}
	
	public static final boolean isValidDate(String start,String end)
	{
		
		
		try {
			Date d_start = s_date_format.parse(start);
			Date d_end = s_date_format.parse(end);			
			return isValidDate(d_start, d_end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static final Date convertToDate(String time)
	{
		try {
			return s_date_format.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得今天的0点的毫秒数。
	 * @return
	 */
	public static final long getTodayMorning(){ 
		return getMorning(System.currentTimeMillis());
	} 
	/**
	 * 获得指定millis的0点
	 * @param millis
	 * @return
	 */
	public static final long getMorning(long millis){ 
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(millis);
		cal.set(Calendar.HOUR_OF_DAY, 0); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTimeInMillis(); 
	} 
	
	public static final long getNextDayMorning(int next){
		
		long today = getTodayMorning();
		
		return today + next * DAY_MILLIS;
	}
	
	/**
	 * 格式化为时：分：秒
	 * @param seconds
	 * @return
	 */
	public static final String formatHourMinuteSecond(int seconds){
		int hour = seconds / 3600;
		int minutes = (seconds  - hour * 3600)/60;
		return hour+":"+minutes+":"+(seconds % 60);
	}
	
	
	public static final void main(String[] args){
		//System.out.println(formatHourMinuteSecond(3500));
		long t = getNextDayMorning(1);
		Date d = new Date(t);
		System.out.println(getDate(d));
		
	}
}
