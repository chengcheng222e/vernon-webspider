/**
 *
 */
package com.vernon.webspider.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理的工具类
 * 
 * @author Vernon.Chen
 *
 */
public class DateUtil {
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 当前日期
	 * 
	 * @return Date
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 设置时间
	 * 
	 * @param date
	 *            Date
	 * @param field
	 *            时间段
	 * @param amount
	 *            数量
	 * @return Date
	 */
	public static Date set(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(field, amount);
		return calendar.getTime();
	}

	/**
	 * 时间增加
	 * 
	 * @param date
	 *            Date
	 * @param field
	 *            时间段
	 * @param amount
	 *            数量
	 * @return Date
	 */
	public static Date add(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 时间增加
	 * 
	 * @param field
	 *            时间段
	 * @param amount
	 *            数量
	 * @return Date
	 */
	public static Date add(int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 按照format指定的格式,格式化字符串为时间类型,如果格式化失败返回null
	 * 
	 * @param str
	 *            时间字符串
	 * @param format
	 *            格式
	 * @return Date
	 */
	public static Date string2Date(String str, String format) {
		try {
			if (str != null) {
				return new SimpleDateFormat(format).parse(str);
			}
		} catch (ParseException ignored) {
		}
		return null;
	}

	/**
	 * 按照format指定的格式,格式化字符串为时间类型,如果格式化失败返回null
	 * 
	 * @param str
	 *            时间字符串
	 * @param format
	 *            格式
	 * @param defaultValue
	 *            默认时间
	 * @return Date
	 */
	public static Date string2Date(String str, String format, Date defaultValue) {
		try {
			if (str != null) {
				return new SimpleDateFormat(format).parse(str);
			}
		} catch (ParseException ignored) {
		}
		return defaultValue;
	}

	/**
	 * 日期串格式化
	 * 
	 * @param str
	 * @param format
	 * @return
	 */
	public static String formatDateString(String str, String format) {
		return date2String(string2Date(str, format), format);
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date formatDate(Date date, String format) {
		return string2Date(date2String(date, format), format);
	}

	/**
	 * 按照format指定的格式,格式化字符串为时间类型,如果格式化失败返回null
	 * 
	 * @param date
	 *            时间
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String date2String(Date date, String format) {
		if (date != null) {
			return new SimpleDateFormat(format).format(date);
		}
		return null;
	}

	/**
	 * 默认日期格式
	 * 
	 * @param date
	 * @return
	 */
	public static String date2StringDefault(Date date) {
		return date2String(date, FORMAT_DATE);
	}

	/**
	 * 标准输出日期格式化,当日的不显示日期,本年的不显示年份
	 * 
	 * @param date
	 *            时间
	 * @return String
	 */
	public static String date2String(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		StringBuffer format = new StringBuffer();
		if (dateCalendar.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)) {
			format.append("yy-MM-dd ");
		} else if (dateCalendar.get(Calendar.DAY_OF_YEAR) != calendar.get(Calendar.DAY_OF_YEAR)) {
			format.append("MM-dd ");
		}
		format.append("HH:mm:ss");
		return date2String(date, format.toString());
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 *            时间
	 * @param postfix
	 *            修饰词(e.g:xxx之前)
	 * @return String
	 */
	public static String dateDiff2String(Date date, String postfix) {
		long longTime = date.getTime();
		long aa = System.currentTimeMillis() - longTime;
		if (postfix == null) {
			postfix = "";
		}
		if (aa <= 1000) {
			return "1秒" + postfix;
		}
		long bb = aa / 1000;// 秒
		if (bb < 60) {
			return bb + "秒" + postfix;
		}
		long cc = bb / 60;// minitue
		if (cc < 60) {
			return cc + "分钟" + postfix;
		}
		long dd = cc / 60;
		if (dd < 24) {
			return dd + "小时" + postfix;
		}
		long ee = dd / 24;
		if (ee < 7) {
			return ee + "天" + postfix;
		}
		long ff = ee / 7;
		if (ff <= 4) {
			return ff + "周" + postfix;
		}
		return date2String(date);
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 *            时间
	 * @param postfix
	 *            修饰
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String dateDiff2String(Date date, String postfix, String format) {
		long longTime = date.getTime();
		long aa = System.currentTimeMillis() - longTime;
		if (postfix == null) {
			postfix = "";
		}
		if (aa <= 1000) {
			return "1秒" + postfix;
		}
		long bb = aa / 1000;// 秒
		if (bb < 60) {
			return bb + "秒" + postfix;
		}
		long cc = bb / 60;// minitue
		if (cc < 60) {
			return cc + "分钟" + postfix;
		}
		long dd = cc / 60;
		if (dd < 24) {
			return dd + "小时" + postfix;
		}
		long ee = dd / 24;
		if (ee < 7) {
			return ee + "天" + postfix;
		}
		long ff = ee / 7;
		if (ff <= 4) {
			return ff + "周" + postfix;
		}
		return date2String(date, format);
	}

}
