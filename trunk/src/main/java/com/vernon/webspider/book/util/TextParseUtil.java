/**
 * 
 */
package com.vernon.webspider.book.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本解析处理的工具类,用作基于文本的处理
 * 
 * @author Vernon.Chen
 *
 */
public class TextParseUtil {

	/**
	 * get Matcher by source and regex
	 * 
	 * @param source
	 * @param regex
	 * @return
	 */
	protected final static Matcher getMatcher(String source, String regex) {
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(new StringBuilder(source));
	}

	/**
	 * parse, return all
	 * 
	 * @param source
	 * @param regex
	 * @return
	 */
	public final static String[] parse(String source, String regex, int groupIndex) {
		try {
			Matcher matcher = TextParseUtil.getMatcher(source, regex);
			int cursor = 0;
			String[] values = new String[16];
			while (matcher.find()) {
				if (cursor == values.length) {
					String[] swap = new String[values.length + values.length / 2];
					System.arraycopy(values, 0, swap, 0, values.length);
					values = swap;
				}
				values[cursor] = matcher.group(groupIndex);
				cursor++;
			}
			if (cursor < values.length) {
				String[] swap = new String[cursor];
				System.arraycopy(values, 0, swap, 0, cursor);
				values = swap;
			}
			return values;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * parse return single by index
	 * 
	 * @param source
	 * @param regex
	 * @param index
	 * @param groupIndex
	 * @return
	 */
	public final static String parse(String source, String regex, int index, int groupIndex) {
		Matcher matcher = TextParseUtil.getMatcher(source, regex);
		if (index < 0) {
			index = 0;
		}
		try {
			if (matcher.find(index)) {
				return matcher.group(groupIndex);
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return "";
	}

}
