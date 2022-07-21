package com.hm.tzgis.util;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public final class StringUtils {

	/**
	 * @param des
	 * @param orig
	 * @return des字符在orig中出现的次数
	 */
	public static int count(String des, String orig) {
		int cnt = 0;
		int offset = 0;
		while ((offset = orig.indexOf(des, offset)) != -1) {
			offset = offset + des.length();
			cnt++;
		}
		return cnt;
	}

	/**
	 * 集合为空
	 * 
	 * @param coll
	 * @return
	 */
	public static boolean isEmpty(Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}

	/**
	 * 数组为空
	 * 
	 * @param
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * Map为空
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * 字符串为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigital(String str) {
		String regex = "([0-9]\\d+))$";
		if (!isEmpty(str) && Pattern.matches(regex, str)) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串首字母大写
	 * 
	 * @param name
	 * @return
	 */
	public static String upperFirstLetter(String name) {
		if (name != null && name.trim().length() != 0)
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		return null;
	}

	/**
	 * 字符串首字母小写
	 * 
	 * @param name
	 * @return
	 */
	public static String lowerFirstLetter(String name) {
		if (name != null && name.trim().length() != 0)
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		return null;
	}

	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	/**
	 * 验证字符串长度是否合法
	 * @param min
	 * @param max
	 * @param text
	 * @return
	 */
	public static boolean hasLength(int min, int max, String text) throws IllegalArgumentException{
		if (min < 0 || min > max) {
			throw new IllegalArgumentException("invalid argument min or max.");
		}
		if (isEmpty(text)) {
			return min == 0;
		}
		text = text.trim();
		return !(text.length() < min || text.length() > max);
	}
	
	/**
	 * 解释整数
	 * 
	 * @param param
	 *            字符串
	 * @param defValue
	 *            省却值
	 * @return Description of the Returned Value
	 */
	public static int parseInt(String param, int defValue) {
		int i = defValue;
		try {
			i = Integer.parseInt(param);
		} catch (Exception e) {
		}
		return i;
	}
}
