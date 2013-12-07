package com.vernon.webspider.book.util;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class SpiderUrlComparator
	implements Comparator {

	private String regex;

	public SpiderUrlComparator(String regex) {
		this.regex = regex;
	}

	@Override
	public int compare(Object o1, Object o2) {
		String spiderUrl1 = (String) o1;
		String spiderUrl2 = (String) o2;
		String strNum1 = TextParseUtil.parse(spiderUrl1, regex, 0, 1);
		String strNum2 = TextParseUtil.parse(spiderUrl2, regex, 0, 1);
		int num1 = Integer.parseInt(strNum1);
		int num2 = Integer.parseInt(strNum2);
		if (num1 < num2) {
			return 0;
		}
		return 1;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
}
