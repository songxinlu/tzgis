package com.hm.tzgis.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 与日期、时间相关的一些常用工具方法.
 * <p>
 * 日期(时间)的常用格式(formater)主要有: <br>
 * yyyy-MM-dd HH:mm:ss <br>
 * 
 * @author
 * @version 1.0.0
 */
public final class DateUtil {

	private static DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat formatU = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private static DateFormat formatUti = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat formatUTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private static DateFormat formatUda = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 对日期(时间)中的日进行加减计算. <br>
	 * 例子: <br>
	 * 如果Date类型的d为 2005年8月20日,那么 <br>
	 * calculateByDate(d,-10)的值为2005年8月10日 <br>
	 * 而calculateByDate(d,+10)的值为2005年8月30日 <br>
	 * 
	 * @param d      日期(时间).
	 * @param amount 加减计算的幅度.+n=加n天;-n=减n天.
	 * @return 计算后的日期(时间).
	 */
	public static Date calculateByDate(Date d, int amount) {
		return calculate(d, Calendar.DAY_OF_MONTH, amount);
	}

	public static Date calculateByHours(Date d, int amount) {
		return calculate(d, GregorianCalendar.HOUR_OF_DAY, amount);
	}

	public static Date calculateByMinute(Date d, int amount) {
		return calculate(d, GregorianCalendar.MINUTE, amount);
	}

	public static Date calculateByMonth(Date d, int amount) {
		return calculate(d, Calendar.MONTH, amount);
	}

	public static Date calculateByYear(Date d, int amount) {
		return calculate(d, Calendar.YEAR, amount);
	}

	/**
	 * 对日期(时间)中由field参数指定的日期成员进行加减计算. <br>
	 * 例子: <br>
	 * 如果Date类型的d为 2005年8月20日,那么 <br>
	 * calculate(d,GregorianCalendar.YEAR,-10)的值为1995年8月20日 <br>
	 * 而calculate(d,GregorianCalendar.YEAR,+10)的值为2015年8月20日 <br>
	 * 
	 * @param d      日期(时间).
	 * @param field  日期成员. <br>
	 *               日期成员主要有: <br>
	 *               年:GregorianCalendar.YEAR <br>
	 *               月:GregorianCalendar.MONTH <br>
	 *               日:GregorianCalendar.DATE <br>
	 *               时:GregorianCalendar.HOUR <br>
	 *               分:GregorianCalendar.MINUTE <br>
	 *               秒:GregorianCalendar.SECOND <br>
	 *               毫秒:GregorianCalendar.MILLISECOND <br>
	 * @param amount 加减计算的幅度.+n=加n个由参数field指定的日期成员值;-n=减n个由参数field代表的日期成员值.
	 * @return 计算后的日期(时间).
	 */
	private static Date calculate(Date d, int field, int amount) {
		if (d == null) {
			d = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 日期(时间)转化为字符串.
	 * 
	 * @param formater 日期或时间的格式.
	 * @param aDate    java.util.Date类的实例.
	 * @return 日期转化后的字符串.
	 */
	public static String date2String(String formater, Date aDate) {
		if (formater == null || "".equals(formater))
			return null;
		if (aDate == null)
			return null;
		return (new SimpleDateFormat(formater)).format(aDate);
	}

	/**
	 *
	 * @param formater
	 * @return
	 */
	public static String timestamp2String(Timestamp formater) {
		if (formater == null || "".equals(formater))
			return null;
		return formatTime.format(formater);
	}

	/**
	 * 当前日期(时间)转化为字符串.
	 * 
	 * @param formater 日期或时间的格式.
	 * @return 日期转化后的字符串.
	 */
	public static String date2String(String formater) {
		return date2String(formater, new Date());
	}

	/**
	 * 获取当前日期对应的星期数. <br>
	 * 1=星期天,2=星期一,3=星期二,4=星期三,5=星期四,6=星期五,7=星期六
	 * 
	 * @return 当前日期对应的星期数
	 */
	public static int dayOfWeek() {
		GregorianCalendar g = new GregorianCalendar();
		int ret = g.get(Calendar.DAY_OF_WEEK);
		g = null;
		return ret;
	}

	/**
	 * 获取所有的时区编号. <br>
	 * 排序规则:按照ASCII字符的正序进行排序. <br>
	 * 排序时候忽略字符大小写.
	 * 
	 * @return 所有的时区编号(时区编号已经按照字符[忽略大小写]排序).
	 */
	public static String[] fecthAllTimeZoneIds() {
		Vector<String> v = new Vector<String>();
		String[] ids = TimeZone.getAvailableIDs();
		for (int i = 0; i < ids.length; i++) {
			v.add(ids[i]);
		}
		java.util.Collections.sort(v, String.CASE_INSENSITIVE_ORDER);
		v.copyInto(ids);
		v = null;
		return ids;
	}

	/**
	 * 将当前的日期时间字符串根据转换为指定时区的日期时间.
	 * 
	 * @param srcFormater   待转化的日期时间的格式.
	 * @param srcDateTime   待转化的日期时间.
	 * @param dstFormater   目标的日期时间的格式.
	 * @param dstTimeZoneId 目标的时区编号.
	 * 
	 * @return 转化后的日期时间.
	 */
	public static String string2Timezone(String srcFormater, String srcDateTime, String dstFormater,
			String dstTimeZoneId) {
		if (srcFormater == null || "".equals(srcFormater))
			return null;
		if (srcDateTime == null || "".equals(srcDateTime))
			return null;
		if (dstFormater == null || "".equals(dstFormater))
			return null;
		if (dstTimeZoneId == null || "".equals(dstTimeZoneId))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
		try {
			// int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(srcDateTime));
			int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
			cal.add(Calendar.MILLISECOND, -zoneOffset);
			return date2String(dstFormater, cal.getTime());
		} catch (ParseException e) {
			return null;
		} finally {
			sdf = null;
		}
	}

	/**
	 * 获取系统当前默认时区与UTC的时间差.(单位:毫秒)
	 * 
	 * @return 系统当前默认时区与UTC的时间差.(单位:毫秒)
	 */
	private static int getDefaultTimeZoneRawOffset() {
		return TimeZone.getDefault().getRawOffset();
	}

	/**
	 * 获取指定时区与UTC的时间差.(单位:毫秒)
	 * 
	 * @param timeZoneId 时区Id
	 * @return 指定时区与UTC的时间差.(单位:毫秒)
	 */
	private static int getTimeZoneRawOffset(String timeZoneId) {
		return TimeZone.getTimeZone(timeZoneId).getRawOffset();
	}

	/**
	 * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)
	 * 
	 * @param timeZoneId 时区Id
	 * @return 系统当前默认时区与指定时区的时间差.(单位:毫秒)
	 */
	private static int getDiffTimeZoneRawOffset(String timeZoneId) {
		return TimeZone.getDefault().getRawOffset() - TimeZone.getTimeZone(timeZoneId).getRawOffset();
	}

	/**
	 * 将日期时间字符串根据转换为指定时区的日期时间.
	 * 
	 * @param srcDateTime   待转化的日期时间.
	 * @param dstTimeZoneId 目标的时区编号.
	 * 
	 * @return 转化后的日期时间.
	 * @see #string2Timezone(String, String, String, String)
	 */
	public static String string2TimezoneDefault(String srcDateTime, String dstTimeZoneId) {
		return string2Timezone("yyyy-MM-dd HH:mm:ss", srcDateTime, "yyyy-MM-dd'T'HH:mm:ss'Z'", dstTimeZoneId);
	}

	/**
	 * 
	 * @param utcTime 格式yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public synchronized static String stringUTC2Date(String utcTime) {

		Calendar calendar = Calendar.getInstance();

		try {
			if (utcTime.contains(".")) {

				calendar.setTime(formatU.parse(utcTime));
			} else {
				DateFormat formatUTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				Date d = formatUTime.parse(utcTime);
				calendar.setTime(d);
			}

			calendar.add(Calendar.HOUR_OF_DAY, 8);
			return formatTime.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(utcTime);
		}
		return null;
	}
//	/**
//	 * 
//	 * @param utcTime 格式yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
//	 * @return yyyy-MM-dd HH:mm:ss
//	 */
//	public synchronized static String stringToUTC2Date(String utcTime) {
//		
//		Calendar calendar = Calendar.getInstance();
//		
//		try {
//			if (utcTime.contains(".")) {
//				
//				calendar.setTime(formatUti.parse(utcTime));
//			} else {
//				
//				calendar.setTime(formatUda.parse(utcTime));
//			}
//			
//			calendar.add(Calendar.HOUR_OF_DAY, 8);
//			return formatDay.format(calendar.getTime());
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(utcTime);
//		}
//		return null;
//	}

	public synchronized static long getDateFromUTC(String utcTime) {

		Calendar calendar = Calendar.getInstance();

		try {
			if (utcTime.contains(".")) {

				calendar.setTime(formatU.parse(utcTime));
			} else {

				calendar.setTime(formatUTime.parse(utcTime));
			}

			calendar.add(Calendar.HOUR_OF_DAY, 8);
			return calendar.getTime().getTime();

		} catch (Exception e) {
			System.out.println(utcTime);
		}
		return 0L;
	}

	/**
	 * 获取一天的起始时间
	 * 
	 * @param date yyyy-MM-dd
	 * @return yyyy-MM-dd 00:00:00
	 */
	public synchronized static String getStartTime(String date) {
		Calendar dayStart = Calendar.getInstance();
		try {
			if (date == null || "".equals(date)) {
				dayStart.setTime(formatDay.parse(formatDay.format(new Date())));
			} else {
				dayStart.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dayStart.set(Calendar.HOUR, 0);
		dayStart.set(Calendar.MINUTE, 0);
		dayStart.set(Calendar.SECOND, 0);
		dayStart.set(Calendar.MILLISECOND, 0);
		return formatTime.format(dayStart.getTime());
	}

	/**
	 * 获取一天的结束时间
	 * 
	 * @param date yyyy-MM-dd
	 * @return yyyy-MM-dd 23:59:59
	 */
	public synchronized static String getEndTime(String date) {
		Calendar dayEnd = Calendar.getInstance();
		try {
			if (date == null || "".equals(date)) {
				dayEnd.setTime(formatDay.parse(formatDay.format(new Date())));
			} else {
				dayEnd.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dayEnd.set(Calendar.HOUR, 23);
		dayEnd.set(Calendar.MINUTE, 59);
		dayEnd.set(Calendar.SECOND, 59);
		dayEnd.set(Calendar.MILLISECOND, 999);
		return formatTime.format(dayEnd.getTime());
	}

	/**
	 * 获取一天的起始时间
	 * 
	 * @param date
	 * @return time
	 */
	public static Long getStartTimeLong(String date) {
		Calendar startEnd = Calendar.getInstance();
		if (date == null || "".equals(date)) {
			startEnd.setTime(new Date());
		} else {
			try {
				startEnd.setTime(formatDay.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		startEnd.set(Calendar.HOUR, 0);
		startEnd.set(Calendar.MINUTE, 0);
		startEnd.set(Calendar.SECOND, 0);
		startEnd.set(Calendar.MILLISECOND, 0);
		return startEnd.getTime().getTime();
	}

	/**
	 * 获取一天的结束时间
	 * 
	 * @param date
	 * @return time
	 */
	public static Long getEndTimeLong(String date) {
		Calendar dayEnd = Calendar.getInstance();
		if (date == null || "".equals(date)) {
			dayEnd.setTime(new Date());
		} else {
			try {
				dayEnd.setTime(formatDay.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		dayEnd.set(Calendar.HOUR, 23);
		dayEnd.set(Calendar.MINUTE, 59);
		dayEnd.set(Calendar.SECOND, 59);
		dayEnd.set(Calendar.MILLISECOND, 999);
		return dayEnd.getTime().getTime();
	}

	/**
	 * 获取一个月的第一天
	 * 
	 * @param date
	 * @return time
	 */
	public static String getMonthFirstDay(String date) {

		Calendar cal = Calendar.getInstance();// 获取当前日期
		try {
			if (date == null || "".equals(date)) {
				cal.setTime(new Date());
			} else {
				cal.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}

	/**
	 * 获取上月的第一天
	 *
	 * @return time
	 */
	public static String getYesMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// 将小时至0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		calendar.set(Calendar.MINUTE, 0);
		// 将秒至0
		calendar.set(Calendar.SECOND, 0);
		// 将毫秒至0
		calendar.set(Calendar.MILLISECOND, 0);
		// 获得当前月第一天
		Date sdate = calendar.getTime();
		String firstDay = formatTime.format(sdate);
		return firstDay;
	}
	/**
	 * 根据传的时间获取上月的第一天
	 *
	 * @return time
	 */
	public static String getMonthFirstDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// 将小时至0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		calendar.set(Calendar.MINUTE, 0);
		// 将秒至0
		calendar.set(Calendar.SECOND, 0);
		// 将毫秒至0
		calendar.set(Calendar.MILLISECOND, 0);
		// 获得当前月第一天
		Date sdate = calendar.getTime();
		String firstDay = formatTime.format(sdate);
		return firstDay;
	}

	/**
	 * 获取上月的最后一天
	 * 
	 * @return time
	 */
	public static String getYesMonthLastDay() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// 将小时至0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		calendar.set(Calendar.MINUTE, 0);
		// 将秒至0
		calendar.set(Calendar.SECOND, 0);
		// 将毫秒至0
		calendar.set(Calendar.MILLISECOND, 0);
		// 将当前月加1；
		calendar.add(Calendar.MONTH, 1);
		// 在当前月的下一月基础上减去1毫秒
		calendar.add(Calendar.MILLISECOND, -1);
		// 获得当前月最后一天
		Date edate = calendar.getTime();
		String lastDay = formatTime.format(edate);
		return lastDay;
	}
	/**
	 * 根据传的时间获取上月的最后一天
	 * 
	 * @return time
	 */
	public static String getMonthLastDayDate(Date date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// 将小时至0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		calendar.set(Calendar.MINUTE, 0);
		// 将秒至0
		calendar.set(Calendar.SECOND, 0);
		// 将毫秒至0
		calendar.set(Calendar.MILLISECOND, 0);
		// 将当前月加1；
		calendar.add(Calendar.MONTH, 1);
		// 在当前月的下一月基础上减去1毫秒
		calendar.add(Calendar.MILLISECOND, -1);
		// 获得当前月最后一天
		Date edate = calendar.getTime();
		String lastDay = formatTime.format(edate);
		return lastDay;
	}

	/**
	 * 获取最近时间(天)
	 * 
	 * @param date
	 * @return time
	 */
	public static String getNearDay(String date, int value) {

		Calendar cal = Calendar.getInstance();// 获取当前日期
		try {
			if (date == null || "".equals(date)) {
				cal.setTime(new Date());
				cal.add(Calendar.DATE, -value);
			} else {
				cal.setTime(formatDay.parse(date));
				cal.add(Calendar.DATE, -value);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}

	/**
	 * 获取最近时间(月)
	 * 
	 * @param date
	 * @return time
	 */
	public static String getNearMonth(String date, int value) {

		Calendar cal = Calendar.getInstance();// 获取当前日期
		try {
			if (date == null || "".equals(date)) {
				cal.setTime(new Date());
				cal.add(Calendar.MONTH, -value);
			} else {
				cal.setTime(formatDay.parse(date));
				cal.add(Calendar.MONTH, -value);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}

	/**
	 * 获取一个月的第一天
	 * 
	 * @param date
	 * @return time
	 */
	public static String getMonthFirstDay(Date date) {

		Calendar cal = Calendar.getInstance();// 获取当前日期
		if (null == date) {
			cal.setTime(new Date());
		} else {
			cal.setTime(date);
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}

	/**
	 * 获取一个月的最后一天
	 * 
	 * @param date
	 * @return time
	 */
	public static String getMonthLastDay(String date) {

		Calendar cal = Calendar.getInstance();// 获取当前日期
		try {
			if (date == null || "".equals(date)) {
				cal.setTime(new Date());
			} else {
				cal.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}

	/**
	 * 获取一个月的最后一天
	 * 
	 * @param date
	 * @return time
	 */
	public static String getMonthLastDay(Date date) {

		Calendar cal = Calendar.getInstance();// 获取当前日期
		if (null == date) {
			cal.setTime(new Date());
		} else {
			cal.setTime(date);
		}
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}

	/**
	 * 获取一个月的时间差
	 * 
	 * @param date
	 * @return time
	 */
	public static Long getMonthSpread(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 0); // 输入类型为int类型

		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		return 60000L * 60L * 24L * (long) dayOfMonth;
	}

	/**
	 * 获取一年的第一天
	 * 
	 * @param date
	 * @return time
	 */
	public static String getYearFirstDay(String date) {

		Calendar cal = Calendar.getInstance();// 获取当前日期
		try {
			if (date == null || "".equals(date)) {
				cal.setTime(formatDay.parse(getSysDate()));
			} else {
				cal.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}

	/**
	 * 获取一年的最后一天
	 * 
	 * @param date
	 * @return time
	 */
	public static String getYearLastDay(String date) {

		Calendar cal = Calendar.getInstance();// 获取当前日期
		try {
			if (date == null || "".equals(date)) {
				cal.setTime(new Date());
			} else {
				cal.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getDayFormat(String date) {
		DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");

		if (date == null || "".equals(date)) {
			return formatDay.format(new Date());
		} else {
			return date;
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthFormat(String date) {
		DateFormat formatDay = new SimpleDateFormat("yyyy-MM");

		if (date == null || "".equals(date)) {
			return formatDay.format(new Date());
		} else {
			try {
				return formatDay.format(formatDay.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getYearFormat(String date) {
		DateFormat formatDay = new SimpleDateFormat("yyyy");

		if (date == null || "".equals(date)) {
			return formatDay.format(new Date());
		} else {
			try {
				return formatDay.format(formatDay.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 判断是否为当天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(String date) {
		if (StringUtils.isEmpty(date)) {
			return true;
		}
		if (date.equals(formatDay.format(new Date()).toString())) {// 格式化为相同格式
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否是今年
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isThisYear(String date) {
		Calendar calendar = Calendar.getInstance();
		int thisYear = calendar.get(Calendar.YEAR);
		try {
			calendar.setTime(formatDay.parse(date));
			int sourceYear = calendar.get(Calendar.YEAR);
			if (thisYear == sourceYear) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
	}

	/**
	 * 判断是否是当月
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isThisMonth(String date) {
		Calendar calendar = Calendar.getInstance();
		int thisMonth = calendar.get(Calendar.MONTH);
		try {
			calendar.setTime(formatDay.parse(date));
			int sourceMonth = calendar.get(Calendar.MONTH);
			if (thisMonth == sourceMonth) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
	}

	/**
	 * 获取开始日期和结束日期之间的天数
	 * 
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public static int getDaysBetweenStartDayAndEndDay(String startDay, String endDay) {
		Calendar calendar = Calendar.getInstance();
		try {
			if (StringUtils.isEmpty(startDay) || StringUtils.isEmpty(endDay)) {
				return 1;
			}
			calendar.setTime(formatDay.parse(startDay));
			long startDayLong = calendar.getTime().getTime();
			calendar.setTime(formatDay.parse(endDay));
			long endDayLong = calendar.getTime().getTime();
			long distance = endDayLong - startDayLong;
			int days = (int) (distance / 1000 / 3600 / 24) + 1;
			return days;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 1;
	}
    
    /**
     * 前一天
     * @return
     */
    public static String  getLastDay(String dateStr) {  
    	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
    	Date date = new Date();
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        return formatDay.format(calendar.getTime()); 
    }  

	/**
	 * 前一天
	 * 
	 * @return
	 */
	public static String getLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date date = calendar.getTime();
		return formatDay.format(date);
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getSysDateMinute() {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(day);
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getSysDate() {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(day);
	}

	/**
	 * 获取本周的开始时间
	 * 
	 * @return
	 */
	public static Date getBeginDayOfWeek() {
		Date date = new Date();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return getDayStartTime(cal.getTime());
	}

	public static Timestamp getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d)
			calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 日期转字符串
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStr(Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 获取day天之后是哪天 yyyy-MM-dd
	 * 
	 * @param day
	 * @return
	 */
	public static String getDay(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, day);
		Date date = calendar.getTime();
		return formatDay.format(date);
	}

	/**
	 * 获取本月开始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Long getMonthBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 设置为1号,当前日期既为本月第一天
		c.set(Calendar.DAY_OF_MONTH, 1);
		// 将小时至0
		c.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		c.set(Calendar.MINUTE, 0);
		// 将秒至0
		c.set(Calendar.SECOND, 0);
		// 将毫秒至0
		c.set(Calendar.MILLISECOND, 0);
		// 获取本月第一天的时间戳
		return c.getTimeInMillis();
	}

	/**
	 * 时间戳转日期（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param millSec
	 * @return
	 */
	public static String transferLongToDate(Long millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(millSec);
		return sdf.format(date);
	}
	
	/**
	 * 日期转时间戳（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static Long DateTotransferLong(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = sdf.parse(date);
		return time.getTime();
	}
	/**
	 * 两个时间之间相差多少分钟
	 * 
	 * @param endDate
	 * @param nowDate
	 * @return
	 */
	public static long getDatePoor(Date maxDate, Date minDate) throws ParseException {
		long between = (maxDate.getTime() - minDate.getTime()) / 1000;
		long min = between / 60;
		return min;
	}

	/**
	 * 根据传的时间计算前一天
	 * 
	 * @return
	 */
	public static String getLastDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date time = calendar.getTime();
		return formatDay.format(time);
	}
	/**
	 * 月份循环拼接字符串时使用
	 * @param i
	 * @return
	 */
	public static String monthAddZero(int i) {
		String month = "";
		if (i>=0 && i<10) {
			month = "0" + i;
		}else {
			month = month + i;
		}
		return month;
	}
	/**
	 * UTC时间格式化为yyyy-MM-dd HH:mm:ss
	 * @param UTCtime
	 * @return
	 */
	public static String UTCTimeFormat(String UTCtime) {
		String month = "";
		try {
			Date date = formatUTime.parse(UTCtime);
			month = formatTime.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return month;
	}
	
	/**
	 * 当前时间是本年的第几个月
	 * @param UTCtime
	 * @return
	 */
	public static int monthOfYear(String date) {
		int day = Integer.parseInt(date.substring(5, 7));
		return day;
	}
	
	/**
	 * UTC时间转北京时间（不加8个小时）直接转 
	 * group by time()类型 SQL 专用！！！！！！！！
	 * 数据格式如下  2019-07-01T00:00:00+08:00
	 * 把+8:00替换为Z 然后直接格式化为yyyy-MM-dd HH:mm:ss
	 * @param UTCtime
	 * @return
	 */
	public synchronized static String groupByTimeSQLUTCFormat(String UTCtime) {
		UTCtime = UTCtime.replace(UTCtime.substring(UTCtime.length()-6, UTCtime.length()),"Z");
		String month = "";
		try {
			DateFormat formatUTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date date = formatUTime.parse(UTCtime);
			month = formatTime.format(date);
		} catch (ParseException e) {
			System.err.println("utctime:"+UTCtime);
			e.printStackTrace();
		}
		
		return month;
	}
	
	public static void main(String[] argc) throws ParseException {
		String string = "2019-07-17T04:30:00Z";
		System.out.println(stringUTC2Date(string));
	}

	/**
	 * 随机生成n到m之间的数据
	 * @param n
	 * @param m
	 * @return
	 */
    public static double getRandom(double n, double m) {
        return Math.random() * (m - n) + n;
    }
    
    /**
	 * 获取两个日期之间的所有日期
	 * 
	 * @param startTime 开始日期
	 * @param endTime   结束日期
	 * @return
	 */
	public static List<String> getDays(String startTime, String endTime) {

		// 返回的日期集合
		List<String> days = new ArrayList<String>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);

			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);
			tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
			while (tempStart.before(tempEnd)) {
				days.add(dateFormat.format(tempStart.getTime()));
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return days;
	}
	
	/**
     * @Author：
     * @Description：更加输入日期，获取输入日期的前一天
     * @Date：
     * @strData：参数格式：yyyy-MM-dd
     * @return：返回格式：yyyy-MM-dd
     */
    public static String getPreDateByDate(String strData) {
        String preDate = "";
         Calendar c = Calendar.getInstance();
         SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
         Date date = null;
         try {
             date = sdf.parse(strData);
         } catch (ParseException e) {
             e.printStackTrace();
         }
         
         c.setTime(date);
         int day1 = c.get(Calendar.DATE);
         c.set(Calendar.DATE, day1 - 1);
         preDate = sdf.format(c.getTime());
        return preDate;
    }
}
